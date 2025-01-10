package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.request.ConnexionRequest;
import mg.itu.cryptomonnaie.request.InscriptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final RestTemplate restTemplate;

    @Value("${identity-flow.api.url}")
    private String identityFlowApiUrl;

    @RequestMapping("/inscription")
    public String formulaireInscription(Model model) {
        model.addAttribute("inscriptionRequest", new InscriptionRequest());
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscription(@ModelAttribute InscriptionRequest inscriptionRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        ResponseEntity<String> responseEntity = restTemplate.exchange(
            identityFlowApiUrl, HttpMethod.POST, new HttpEntity<>(inscriptionRequest, httpHeaders), String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok("Inscription réussie");
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body("Erreur lors de l'inscription");
        }

        return null;
    }

    @GetMapping("/connexion")
    public String formulaireConnexion(Model model) {
        model.addAttribute("connexionRequest", new ConnexionRequest());
        return "auth/connexion";
    }

    @PostMapping("/connexion")
    public String connexion(@ModelAttribute ConnexionRequest connexionRequest) {
        /* TODO :
            - Utilisation de l'API identity-flow
            - Mettre l'utilisateur en base de données puis en session
        */

        return null;
    }

    @PostMapping("/deconnexion")
    public String deconnexion() {
        // TODO : Utilisation de l'API identity-flow

        return "redirect:/auth/connexion";
    }
}
