package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;

import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cours")
public class CoursCryptoController {
    private final CoursCryptoService   coursCryptoService;
    private final CryptomonnaieService cryptomonnaieService;

    @GetMapping
    public String index(
        Model model,
        @RequestParam(required = false) Integer idCryptomonnaie
    ) {
        Cryptomonnaie cryptomonnaie = cryptomonnaieService.getByIdOrGetFirst(idCryptomonnaie);
        model.addAttribute("coursCryptos", coursCryptoService.getByCryptomonnaie(cryptomonnaie.getId()))
            .addAttribute("cryptomonnaies", cryptomonnaieService.getAll())
            .addAttribute("cryptoActuelle", cryptomonnaie.getDesignation());

        return "cours";
    }
}
