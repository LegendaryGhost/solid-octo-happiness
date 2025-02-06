package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class HistoriqueTransactionController {
    private final HistoriqueTransactionService historiqueTransactionService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/etat")
    public String afficherEtatPortefeuille(Model model) {
        List<HistoriqueCryptoDTO> etatPortefeuilles = historiqueTransactionService.etatPortefeuilleActuel(authenticationManager.safelyGetCurrentUser());
        model.addAttribute("etatPortefeuilles", etatPortefeuilles);

        return "pages/portefeuille/etat_protefeuille";
    }

    @GetMapping("/historique")
    public String historiquePortefeuille(Model model) {
        model.addAttribute("historiques", historiqueTransactionService.historiqueUtilisateur(authenticationManager.safelyGetCurrentUser()));
        return "pages/historique/historique_transactions";
    }

    @GetMapping("/historique-global")
    public String historiqueGlobal(Model model) {
        model.addAttribute("historiques", historiqueTransactionService.historiqueGlobale());
        return "pages/historique/historique_global";
    }
}
