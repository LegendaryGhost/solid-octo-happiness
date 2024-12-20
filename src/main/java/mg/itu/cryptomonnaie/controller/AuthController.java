package mg.itu.cryptomonnaie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @RequestMapping("/inscription")
    public String formulaireInscription() {
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscription() {
        // TODO : Utilisation de l'API identity-flow

        return null;
    }

    @GetMapping("/connexion")
    public String formulaireConnexion() {
        return "auth/connexion";
    }

    @PostMapping("/connexion")
    public String connexion() {
        // TODO : Utilisation de l'API identity-flow

        return null;
    }
}
