package mg.itu.cryptomonnaie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import mg.itu.cryptomonnaie.entity.CoursCrypto;
import mg.itu.cryptomonnaie.entity.Cryptomonnaie;
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

        return "cours";
    }

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<CoursCrypto> getCoursData(@RequestParam(required = false) Integer idCryptomonnaie) {
        return coursCryptoService.getByCryptomonnaie(cryptomonnaieService.getByIdOrGetFirst(idCryptomonnaie).getId());
    }
}
