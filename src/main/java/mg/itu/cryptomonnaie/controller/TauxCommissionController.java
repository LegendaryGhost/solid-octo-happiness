package mg.itu.cryptomonnaie.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.TauxCommission;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCommission;
import mg.itu.cryptomonnaie.enums.TypeAnalyseCoursCrypto;
import mg.itu.cryptomonnaie.request.AnalyseCommissionRequest;
import mg.itu.cryptomonnaie.request.AnalyseCoursCryptoRequest;
import mg.itu.cryptomonnaie.request.TauxCommissionRequest;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;
import mg.itu.cryptomonnaie.service.TauxCommissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/commission")
public class TauxCommissionController {
    private final CryptomonnaieService cryptomonnaieService;
    public final TauxCommissionService tauxCommissionService;

    @GetMapping("/modifier")
    public String modifierCommission(Model model) {
        TauxCommissionRequest tauxCommission = new TauxCommissionRequest();
        model.addAttribute("tauxCommissionRequest",tauxCommission);
        return "commission/modifier_commission";
    }

    @PostMapping("/modifier")
    public String sauvegarderModificationCommission(@Valid TauxCommissionRequest tauxCommission){
        tauxCommissionService.save(tauxCommission);
        return "redirect:/commission/modifier";
    }

    @GetMapping("/analyse")
    public String analyserCommission(Model model) {
        AnalyseCommissionRequest analyseCommissionRequest = new AnalyseCommissionRequest();
        model.addAttribute("analyseCommissionRequest",analyseCommissionRequest);
        model.addAttribute("crypto",cryptomonnaieService.getAll());
        model.addAttribute("typesAnalyse", TypeAnalyseCommission.values());
        model.addAttribute("resultat",0);

        return "commission/analyse_commission";
    }

    @PostMapping("/analyse")
    public String analyseRequestCommission(@Valid AnalyseCommissionRequest request, Model model){

        model.addAttribute("analyseCommissionRequest",request);
        model.addAttribute("crypto",cryptomonnaieService.getAll());
        model.addAttribute("typesAnalyse", TypeAnalyseCommission.values());
        model.addAttribute("resultat",0);

        return "redirect:/commission/analyse";
    }
}
