package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.entity.HistoriqueCrypto;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;
import mg.itu.cryptomonnaie.service.HistoriqueCryptoService;
import mg.itu.cryptomonnaie.service.ProfilService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/portefeuille")
public class HistoriqueCryptoController {
    private final HistoriqueCryptoService historiqueCryptoService;
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
