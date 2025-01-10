package mg.itu.cryptomonnaie.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.request.HistoriqueFondRequest;
import mg.itu.cryptomonnaie.service.HistoriqueFondService;
import mg.itu.cryptomonnaie.service.TypeTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@RequestMapping("/historique-fond")
@Controller
public class HistoriqueFondController {

    private final TypeTransactionService typeTransactionService;
    private final HistoriqueFondService historiqueFondService;

    @GetMapping("/form")
    public String form(Model model) {
	model.addAttribute("historique", new HistoriqueFondRequest());
	model.addAttribute("types", typeTransactionService.liste());

	return "pages/transaction/formulaire_depot_retrait";
    }

    @PostMapping("/ajouter")
    public String ajouter(HistoriqueFondRequest historique) throws MessagingException {
	historiqueFondService.creerHistoriqueFondTemporaire(historique);
	return "redirect:/historique-fond/form";
    }

    @GetMapping("/valider")
    public String validerTransaction(@RequestParam("token") String token) {
	historiqueFondService.confirmerTransaction(token);
	return "redirect:/historique-fond/form";
    }

}
