package mg.itu.cryptomonnaie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/inscription")
    public String inscription() {
        return "auth/inscription";
    }

    @GetMapping("/connexino")
    public String connexion() {
        return "auth/connexion";
    }
}
