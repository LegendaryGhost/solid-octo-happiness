package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueCryptoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class HistoriqueCryptoController {
    private final HistoriqueCryptoService historiqueCryptoService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/etat")
    public String afficherEtatPortefeuille(Model model) {
        List<HistoriqueCryptoDTO> etatPortefeuilles = historiqueCryptoService
                .portefeuilleClientActuel(authenticationManager.safelyGetCurrentUser());
        model.addAttribute("etatPortefeuilles", etatPortefeuilles);

        return "pages/portefeuille/etat_protefeuille";
    }

    @GetMapping("/historique")
    public String historiquePortefeuille(Model model) {
        model.addAttribute("historiques",
                historiqueCryptoService.historiqueUtilisateur(authenticationManager.safelyGetCurrentUser()));
        return "pages/historique/historique_transactions";
    }

    @GetMapping("/historique-global")
    public String historiqueGlobal(Model model) {
        model.addAttribute("historiques", historiqueCryptoService.historiqueGlobale());
        return "pages/historique/historique_global";
    }
}
