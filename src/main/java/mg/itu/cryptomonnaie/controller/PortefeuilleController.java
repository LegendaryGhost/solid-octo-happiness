package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.service.PortefeuilleService;
import mg.itu.cryptomonnaie.utils.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class PortefeuilleController {
    private final PortefeuilleService portefeuilleService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("situationPortefeuille", portefeuilleService.getSituationPortefeuilleActuelle(
            Facade.authenticationManager().safelyGetCurrentUser().getId()
        ));

        return "portefeuille";
    }
}
