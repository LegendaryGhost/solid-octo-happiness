package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cours")
public class CoursCryptoController {
    private final CoursCryptoService   coursCryptoService;
    private final CryptomonnaieService cryptomonnaieService;

    @Value("${courscrypto.fixedrate}")
    private long coursCryptoFixedRate;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("cryptomonnaies", cryptomonnaieService.getAll())
            .addAttribute("timeout",coursCryptoFixedRate);
        return "cours";
    }

    @ResponseBody
    @GetMapping(path = "/donnees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CoursCrypto> donneesCoursCrypto(@RequestParam(required = false) Integer idCryptomonnaie) {
        return coursCryptoService.getByCryptomonnaie(cryptomonnaieService.getByIdOrGetTopest(idCryptomonnaie).getId());
    }
}
