package mg.itu.cryptomonnaie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCoursCrypto;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
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
    private final ObjectMapper objectMapper;

    @GetMapping
    public String index(
        Model model,
        @RequestParam(required = false) Integer idCryptomonnaie
    ) throws JsonProcessingException {
        Cryptomonnaie cryptomonnaie = cryptomonnaieService.getByIdOrGetFirst(idCryptomonnaie);
        model.addAttribute("coursCryptos", objectMapper.writeValueAsString(
            coursCryptoService.getByCryptomonnaie(cryptomonnaie.getId())
            )).addAttribute("cryptomonnaies", cryptomonnaieService.getAll())
            .addAttribute("cryptoActuelle", cryptomonnaie.getDesignation());

        return "cours/cours";
    }

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CoursCrypto> getCoursData(@RequestParam(required = false) Integer idCryptomonnaie) {
        return coursCryptoService.getByCryptomonnaie(cryptomonnaieService.getByIdOrGetFirst(idCryptomonnaie).getId());
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
