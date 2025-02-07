package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueTransactionService;
import mg.itu.cryptomonnaie.service.PortefeuilleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class HistoriqueTransactionController {
    private final HistoriqueTransactionService historiqueTransactionService;
    private final PortefeuilleService portefeuilleService;
    private final AuthenticationManager authenticationManager;
    private final CryptomonnaieService cryptomonnaieService;
    private final ProfilService profilService;

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

    @GetMapping("/historique/profil/{id}")
    public String historiquePortefeuilleProfil(Model model, @PathVariable("id") Long id) {
        model.addAttribute("historiques",
                historiqueCryptoService.historiqueUtilisateur(profilService.getById(id)));
        return "pages/historique/historique_transactions";
    }

    @GetMapping("/historique-global")
    public String historiqueGlobal(Model model) {
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.listeCryptomonnaie();
        List<Profil> profils = profilService.listeProfils();
        model.addAttribute("historiques", historiqueCryptoService.historiqueGlobale())
                .addAttribute("cryptomonnaies", cryptomonnaies)
                .addAttribute("profils", profils);
        return "pages/historique/historique_global";
    }

    @GetMapping("filtrer")
    public String filtrageHistorique(Model model,
            @RequestParam(value = "dateHeureMin", required = false) LocalDateTime dateHeureMin,
            @RequestParam(value = "dateHeureMax", required = false) LocalDateTime dateHeureMax,
            @RequestParam(value = "idCrypto", required = false) Long idCrypto,
            @RequestParam(value = "idProfil", required = false) Long idProfil) {
        List<HistoriqueCrypto> historiques = historiqueCryptoService.filtrerHistorique(dateHeureMin, dateHeureMax,
                idCrypto, idProfil);
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.listeCryptomonnaie();
        List<Profil> profils = profilService.listeProfils();
        model.addAttribute("historiques", historiques)
                .addAttribute("cryptomonnaies", cryptomonnaies)
                .addAttribute("profils", profils);

        return "pages/historique/historique_global";
    }
}
