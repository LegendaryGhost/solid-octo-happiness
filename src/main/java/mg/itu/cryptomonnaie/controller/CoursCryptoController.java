package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cours")
public class CoursCryptoController {
    private final CoursCryptoService coursCryptoService;
    private final CryptomonnaieService cryptomonnaieService;

    @GetMapping("/chart")
    public String afficherEtatPortefeuille(Model model) {
        Cryptomonnaie crypto = cryptomonnaieService.getById(1L); // Bitcoin par défaut
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
    }

    @GetMapping("/charttest")
    public ResponseEntity<List<CoursCrypto>> getCoursCrypto() {
        Cryptomonnaie crypto = cryptomonnaieService.getById(1L); // Bitcoin par défaut
        List<CoursCrypto> coursCryptos = coursCryptoService.listeCoursParCryptomonnaie(crypto);

        if (coursCryptos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(coursCryptos);
        }

        return ResponseEntity.ok(coursCryptos);
    }

}
