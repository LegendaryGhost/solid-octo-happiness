package mg.itu.cryptomonnaie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("commission")
public class TauxCommissionController {
    @GetMapping("/modifier")
    public String modifierCommission(Model model) {
        return "commission/modifier_commission";
    }

    @GetMapping("/analyse")
    public String analyserCommission(Model model) {
        return "commission/analyse_commission";
    }
}
