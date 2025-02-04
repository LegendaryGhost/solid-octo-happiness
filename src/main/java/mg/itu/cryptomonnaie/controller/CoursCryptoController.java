package mg.itu.cryptomonnaie.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cours")
public class CoursCryptoController {
    private final CoursCryptoService coursCryptosService;
    private final CryptomonnaieService cryptomonnaieService;

    @GetMapping("/chart")
    public String afficherEtatPortefeuille(Model model) {
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.listeCryptomonnaie();
        model.addAttribute("cryptomonnaies", cryptomonnaies);
        return "pages/crypto_detail/cours";
    }

    @GetMapping("/chart/traitement/")
    public String traitementChart(Model model, @RequestParam("crypto") Long idCryptomonnaie) {
        Cryptomonnaie crypto = cryptomonnaieService.getById(idCryptomonnaie);
        List<CoursCrypto> coursCryptos = coursCryptosService.listeCoursParCryptomonnaie(crypto);
        model.addAttribute("coursCryptoos", coursCryptos);
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.listeCryptomonnaie();
        model.addAttribute("cryptomonnaies", cryptomonnaies);
        return "pages/crypto_detail/cours";
    }
}
