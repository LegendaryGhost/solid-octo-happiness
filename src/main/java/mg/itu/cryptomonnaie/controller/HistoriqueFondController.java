package mg.itu.cryptomonnaie.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@RequestMapping("/historique-fond")
@Controller
public class HistoriqueFondController {

    @GetMapping("/form")
    public String form() {
	return "pages/transaction/formulaire_depot_retrait";
    }

}
