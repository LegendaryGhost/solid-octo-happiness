package mg.itu.cryptomonnaie.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.utils.Utils;
import mg.itu.cryptomonnaie.request.ConnexionRequest;
import mg.itu.cryptomonnaie.request.InscriptionRequest;
import mg.itu.cryptomonnaie.request.VerificationPinRequest;
import mg.itu.cryptomonnaie.service.ProfilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthenticationController {
    private static final String PENDING_USER_EMAIL_KEY = "pending_user_email";

    private final RestTemplate restTemplate;
    private final ParameterizedTypeReference<Map<String, Object>> mapTypeReference;
    private final ProfilService profilService;

    @Value("${identity-flow.api.url}")
    private String identityFlowApiUrl;

    @GetMapping("/")
    public String index() {
        return "redirect:/connexion";
    }

    @GetMapping("/inscription")
    public String formulaireInscription(Model model) {
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
                new HttpEntity<>(inscriptionRequest, Utils.createJsonHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful())
                redirectAttributes.addFlashAttribute("message",
                    Objects.requireNonNull(responseEntity.getBody()).get("message"));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                Map<String, Object> responseBody = e.getResponseBodyAs(mapTypeReference);
                if (responseBody == null || !responseBody.containsKey("errors"))
                    throw new RuntimeException();

                @SuppressWarnings("unchecked")
                Map<String, List<String>> errors = (Map<String, List<String>>) responseBody.get("errors");
                errors.forEach((field, fieldErrors) ->
                    fieldErrors.forEach(error -> bindingResult.rejectValue(field, "error.", error))
                );

            } else bindingResult.reject("error",
                (String) Objects.requireNonNull(e.getResponseBodyAs(mapTypeReference)).get("error"));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.inscriptionRequest", bindingResult);
            redirectAttributes.addFlashAttribute("inscriptionRequest", inscriptionRequest);

            return "redirect:/inscription";
        }

        return "redirect:/connexion";
    }

    @GetMapping("/connexion")
    public String formulaireConnexion(Model model) {
        model.addAttribute("connexionRequest", new ConnexionRequest());
        return "auth/connexion";
    }

    @PostMapping("/connexion")
    public String connexion(
        @ModelAttribute ConnexionRequest connexionRequest,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        HttpSession httpSession
    ) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/connexion", HttpMethod.POST,
                new HttpEntity<>(connexionRequest, Utils.createJsonHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful())
                httpSession.setAttribute(PENDING_USER_EMAIL_KEY, connexionRequest.getEmail());
        } catch (HttpClientErrorException e) {
            // TODO : Handle this shit
        } catch (HttpServerErrorException e) {
            // TODO : Handle this shit
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.connexionRequest", bindingResult);
            redirectAttributes.addFlashAttribute("connexionRequest", connexionRequest);

            return "redirect:/connexion";
        }

        return "redirect:/verification-code-pin";
    }

    @GetMapping("/verification-code-pin")
    public String pageVerificationCodePin(HttpSession httpSession) {
        String pendingUserEmail = (String) httpSession.getAttribute(PENDING_USER_EMAIL_KEY);
        return pendingUserEmail == null ? "redirect:/connexion" : "auth/confirmation/pin_confirmation";
    }

    @PostMapping("/verification-code-pin")
    public String verificationCodePin(
        @RequestParam Integer codePin,
        HttpSession httpSession,
        RedirectAttributes redirectAttributes
    ) {
        String pendingUserEmail = (String) httpSession.getAttribute(PENDING_USER_EMAIL_KEY);
        if (pendingUserEmail == null) return "redirect:/connexion";

        VerificationPinRequest verificationPinRequest = new VerificationPinRequest(pendingUserEmail, codePin);
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/verification-pin", HttpMethod.POST,
                new HttpEntity<>(verificationPinRequest, Utils.createJsonHttpHeaders()),
                mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                httpSession.removeAttribute(PENDING_USER_EMAIL_KEY);
                Utils.login(pendingUserEmail, profilService, httpSession);

                return ""; // redirection vers la page d'accueil
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED)
                redirectAttributes.addFlashAttribute("error", "Code PIN invalide ou expiré");
            else if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                redirectAttributes.addFlashAttribute("error", "Utilisateur ou code PIN non trouvé");
        } catch (HttpServerErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur inattendue est survenue");
        }

        return "redirect:/verification-code-pin";
    }
}
