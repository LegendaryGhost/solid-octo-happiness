package mg.itu.cryptomonnaie.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.exception.InternalException;
import mg.itu.cryptomonnaie.request.EmailAndPasswordRequest;
import mg.itu.cryptomonnaie.request.InscriptionRequest;
import mg.itu.cryptomonnaie.request.VerificationCodePinRequest;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static mg.itu.cryptomonnaie.utils.Utils.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthenticationController {
    private static final String PENDING_VERIFICATION_EMAIL_KEY = "_pending_verification_email";

    private final UtilisateurService utilisateurService;
    private final RestTemplate restTemplate;
    private final ParameterizedTypeReference<Map<String, Object>> mapTypeReference;
    private final AuthenticationManager authenticationManager;

    @Value("${identity-flow.apiurl}")
    private String identityFlowApiUrl;

    @GetMapping
    public String index() {
        return "redirect:/connexion";
    }

    @GetMapping("/inscription")
    public String formulaireInscription(Model model) {
        if (!model.containsAttribute("inscriptionRequest"))
            model.addAttribute("inscriptionRequest", new InscriptionRequest());

        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscription(
        @ModelAttribute InscriptionRequest inscriptionRequest,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/inscription", HttpMethod.POST,
                new HttpEntity<>(inscriptionRequest, createJsonContentTypeHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful())
                redirectAttributes.addFlashAttribute("success",
                    Objects.requireNonNull(responseEntity.getBody()).get("message"));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Erreur lors d'une inscription d'un utilisateur", e);
            handleHttpStatusCodeException(e, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_KEY_PREFIX + "inscriptionRequest", bindingResult);
            redirectAttributes.addFlashAttribute("inscriptionRequest", inscriptionRequest.unsetMotDePasse());

            return "redirect:/inscription";
        }

        return "redirect:/connexion";
    }

    @GetMapping("/connexion")
    public String formulaireConnexion(Model model) {
        if (!model.containsAttribute("connexionRequest"))
            model.addAttribute("connexionRequest", new EmailAndPasswordRequest());

        return "auth/connexion";
    }

    @PostMapping("/connexion")
    public String connexion(
        @ModelAttribute EmailAndPasswordRequest connexionRequest,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        HttpSession httpSession
    ) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/connexion", HttpMethod.POST,
                new HttpEntity<>(connexionRequest, createJsonContentTypeHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                httpSession.setAttribute(PENDING_VERIFICATION_EMAIL_KEY, connexionRequest.getEmail());
                redirectAttributes.addFlashAttribute("success",
                    Objects.requireNonNull(responseEntity.getBody()).get("message"));
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Erreur lors d'une tentative de connexion d'un utilisateur", e);
            handleHttpStatusCodeException(e, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT_KEY_PREFIX + "connexionRequest", bindingResult);
            redirectAttributes.addFlashAttribute("connexionRequest", connexionRequest.unsetMotDePasse());

            return "redirect:/connexion";
        }

        return "redirect:/verification-code-pin";
    }

    @GetMapping("/verification-code-pin")
    public String pageVerificationCodePin(
        @Nullable @SessionAttribute(name = PENDING_VERIFICATION_EMAIL_KEY, required = false) String pendingVerificationEmail
    ) {
        return pendingVerificationEmail == null ? "redirect:/connexion" : "auth/code_pin_confirmation";
    }

    @PostMapping("/verification-code-pin")
    public String verificationCodePin(
        @RequestParam(name = "codePin") String codePin,
        HttpSession httpSession,
        @Nullable @SessionAttribute(name = PENDING_VERIFICATION_EMAIL_KEY, required = false) String pendingVerificationEmail,
        RedirectAttributes redirectAttributes
    ) {
        if (pendingVerificationEmail == null) return "redirect:/connexion";

        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/verification-pin", HttpMethod.POST,
                new HttpEntity<>(new VerificationCodePinRequest(pendingVerificationEmail, codePin), createJsonContentTypeHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                httpSession.removeAttribute(PENDING_VERIFICATION_EMAIL_KEY);

                @SuppressWarnings("unchecked")
                Map<String, String> data = (Map<String, String>) Objects.requireNonNull(responseEntity.getBody()).get("data");
                final String token = data.get("token");
                authenticationManager.authenticate(utilisateurService.updateOrCreate(
                    pendingVerificationEmail, token, recupererInformationsUtilisateur(token)
                ));

                return "redirect:/portefeuille";
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Erreur lors d'une vérification d'un code pin", e);

            redirectAttributes.addFlashAttribute("error",
                Objects.requireNonNull(e.getResponseBodyAs(mapTypeReference)).get("error"));
        }

        return "redirect:/verification-code-pin";
    }

    @GetMapping("/deconnexion")
    public String deconnexion() {
        authenticationManager.logout();
        return "redirect:/connexion";
    }

    private Map<String, String> recupererInformationsUtilisateur(final String token) {
        try {
            HttpHeaders httpHeaders = createJsonContentTypeHttpHeaders();
            httpHeaders.setBearerAuth(token);

            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/utilisateurs/informations", HttpMethod.GET,
                new HttpEntity<>(httpHeaders), mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                @SuppressWarnings("unchecked")
                Map<String, Map<String, String>> data = ((Map<String, Map<String, String>>) Objects.requireNonNull(responseEntity.getBody()).get("data"));
                if (!data.containsKey("utilisateur"))
                    throw new RuntimeException("Données de l'utilisateur absentes dans le corps de la réponse");

                return data.get("utilisateur");
            } else throw new InternalException();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Erreur lors de la récupération des informations de l'utilisateur", e);
            throw e;
        }
    }

    private void handleHttpStatusCodeException(HttpStatusCodeException e, BindingResult bindingResult) {
        if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
            Map<String, Object> responseBody = e.getResponseBodyAs(mapTypeReference);
            if (responseBody == null || !responseBody.containsKey("errors"))
                throw new RuntimeException("Le corps de la réponse est invalide");

            @SuppressWarnings("unchecked")
            Map<String, List<String>> errors = (Map<String, List<String>>) responseBody.get("errors");
            errors.forEach((field, errorMessages) -> errorMessages
                .forEach(errorMessage ->
                    bindingResult.rejectValue(snakeToCamelCase(field), "error.", errorMessage)
                ));

        } else bindingResult.reject("error",
            (String) Objects.requireNonNull(e.getResponseBodyAs(mapTypeReference)).get("error"));
    }
}
