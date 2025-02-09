package mg.itu.cryptomonnaie.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCoursCrypto;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import mg.itu.cryptomonnaie.service.CoursCryptoService;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;

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

    @GetMapping(path = "/analyse")
    public String analyse(Model model) {
        AnalyseCoursCryptoRequest request = new AnalyseCoursCryptoRequest();
        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.getAll();
        model.addAttribute("request", request);
        model.addAttribute("cryptomonnaies", cryptomonnaies);
        model.addAttribute("typesAnalyse", TypeAnalyseCoursCrypto.values());
        model.addAttribute("resultat",0);
        return "cours/AnalyseCours";
    }

    @PostMapping("/analyse")
    public String traiterAnalyse(@Valid AnalyseCoursCryptoRequest request, Model model) {

        System.out.println(request);

        List<Cryptomonnaie> cryptomonnaies = cryptomonnaieService.getAll();
        model.addAttribute("request", request);
        model.addAttribute("cryptomonnaies", cryptomonnaies);
        model.addAttribute("typesAnalyse", TypeAnalyseCoursCrypto.values());
        model.addAttribute("resultat", coursCryptoService.analyser(request));

        return "redirect:/cours/analyse";
    }
}
