package mg.itu.cryptomonnaie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import mg.itu.cryptomonnaie.dto.HistoriqueCryptoDTO;
import mg.itu.cryptomonnaie.service.HistoriqueCryptoService;

@Controller
@RequestMapping("/portefeuille")
public class HistoriqueCryptoController {

    @Autowired
    private HistoriqueCryptoService historiqueCryptoService;

    @GetMapping("/etat")
    public String afficherEtatPortefeuille(Model model, HttpSession session) {
        List<HistoriqueCryptoDTO> etatPortefeuilles = historiqueCryptoService.portefeuilleClientActuel(session);
        model.addAttribute("etatPortefeuilles", etatPortefeuilles);
        return "pages/portefeuille/etat_protefeuille";
    }
}
