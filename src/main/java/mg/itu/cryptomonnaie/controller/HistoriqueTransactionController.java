package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueTransactionService;
import mg.itu.cryptomonnaie.service.PortefeuilleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class HistoriqueTransactionController {
    private final HistoriqueTransactionService historiqueTransactionService;
    private final PortefeuilleService portefeuilleService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/etat")
    public String afficherEtatPortefeuille(Model model) {
        model.addAttribute("situationPortefeuille",
            portefeuilleService.getSituationPortefeuilleActuelle(authenticationManager.safelyGetCurrentUser()));

        return "pages/portefeuille/etat_protefeuille";
    }

    @GetMapping("/historique")
    public String historiquePortefeuille(Model model) {
        model.addAttribute("historiques", historiqueTransactionService.getAllByUtilisateurIdOrderByDateHeureDesc(authenticationManager.safelyGetCurrentUser().getId()));
        return "pages/historique/historique_transactions";
    }

    @GetMapping("/historique-global")
    public String historiqueGlobal(Model model) {
        model.addAttribute("historiques", historiqueTransactionService.getHistoriqueGlobale());
        return "pages/historique/historique_global";
    }
}
