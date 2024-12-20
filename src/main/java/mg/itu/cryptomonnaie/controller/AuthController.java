package mg.itu.cryptomonnaie.controller;

import mg.itu.cryptomonnaie.request.ConnexionRequest;
import mg.itu.cryptomonnaie.request.InscriptionRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    @RequestMapping("/inscription")
    public String formulaireInscription(Model model) {
        model.addAttribute("inscriptionRequest", new InscriptionRequest());
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscription(@ModelAttribute InscriptionRequest inscriptionRequest) {
        // TODO : Utilisation de l'API identity-flow

        return null;
    }

    @GetMapping("/connexion")
    public String formulaireConnexion(Model model) {
        model.addAttribute("connexionRequest", new ConnexionRequest());
        return "auth/connexion";
    }

    @PostMapping("/connexion")
    public String connexion(@ModelAttribute ConnexionRequest connexionRequest) {
        // TODO : Utilisation de l'API identity-flow

        return null;
    }
}
