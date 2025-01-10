package mg.itu.cryptomonnaie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.Utils;
import mg.itu.cryptomonnaie.request.ConnexionRequest;
import mg.itu.cryptomonnaie.request.InscriptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private static final String PENDING_USER_EMAIL_KEY = "pending_user_email";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${identity-flow.api.url}")
    private String identityFlowApiUrl;

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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/inscription", HttpMethod.POST,
                new HttpEntity<>(inscriptionRequest, httpHeaders), String.class);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED)
                redirectAttributes.addFlashAttribute("message", "Un email de vérification a été envoyé par notre fournisseur d'identité");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() != HttpStatus.UNPROCESSABLE_ENTITY)
                bindingResult.reject("globalError", "Une erreur inattendue est survenue");
            else {
                Map<String, Object> errors = parseErrors(e.getResponseBodyAsString());
                errors.forEach((field, errorMessage) ->
                    bindingResult.rejectValue(field, "error." + field, errorMessage.toString())
                );
            }
        } catch (Exception e) {
            bindingResult.reject("globalError", "Une erreur inattendue est survenue");
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                identityFlowApiUrl + "/auth/connexion", HttpMethod.POST,
                new HttpEntity<>(connexionRequest, httpHeaders), String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK)
                httpSession.setAttribute(PENDING_USER_EMAIL_KEY, connexionRequest.getEmail());
        } catch (HttpClientErrorException e) {
            bindingResult.reject("globalError", e.getStatusCode() == HttpStatus.NOT_FOUND ? "Identifiants incorrects ou utilisateur non trouvé" : "Erreur de communication avec l'API d'identité");
        } catch (Exception e) {
            bindingResult.reject("globalError", "Une erreur inattendue est survenue");
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
        if (pendingUserEmail == null)
            return "redirect:/connexion";

        // TODO: Call API

        return "auth/verification_code_pin";
    }

    @PostMapping("/verification-code-pin")
    public String verificationCodePin(@RequestParam Integer codePin) {
        // TODO: Utilisation de l'API identity-flow

        return null;
    }

    @PostMapping("/deconnexion")
    public String deconnexion() {
        // TODO : Utilisation de l'API identity-flow

        return "redirect:/connexion";
    }

    private Map<String, Object> parseErrors(String responseBody) {
        try {
            Map<String, Object> result = new HashMap<>();
            objectMapper.readTree(responseBody).path("errors")
                .fields()
                .forEachRemaining(entry ->
                    result.put(Utils.toCamelCase(entry.getKey()), entry.getValue().asText())
                );

            return result;
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
