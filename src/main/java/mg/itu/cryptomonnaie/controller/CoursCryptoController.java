package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cours")
public class CoursCryptoController {
    private final CoursCryptoService   coursCryptoService;
    private final CryptomonnaieService cryptomonnaieService;

    /*@GetMapping("/chart")
    public String afficherEtatPortefeuille(Model model) {
        Cryptomonnaie crypto = cryptomonnaieService.getById(1); // Bitcoin par défaut
        List<CoursCrypto> coursCryptos = coursCryptoService.listeCoursParCryptomonnaie(crypto);
        System.out.println("Cours envoyés : " + coursCryptos.size());

        model.addAttribute("coursCryptoos", coursCryptos);
        model.addAttribute("cryptomonnaies", cryptomonnaieService.listeCryptomonnaie());
        model.addAttribute("cryptoActuelle", crypto.getDesignation());

        return "pages/crypto_detail/cours";
    }

    @GetMapping("/chart/traitement")
    public String traitementChart(Model model, @RequestParam("crypto") Long idCryptomonnaie) {
        Cryptomonnaie crypto = cryptomonnaieService.getById(idCryptomonnaie);
        List<CoursCrypto> coursCryptos = coursCryptoService.listeCoursParCryptomonnaie(crypto);

        model.addAttribute("coursCryptoos", coursCryptos);
        model.addAttribute("cryptomonnaies", cryptomonnaieService.listeCryptomonnaie());
        model.addAttribute("cryptoActuelle", crypto.getDesignation());

        return "pages/crypto_detail/cours";
    } */
}
