package mg.itu.cryptomonnaie.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.request.HistoriqueFondRequest;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueFondService;
import mg.itu.cryptomonnaie.service.TypeTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/historique-fond")
public class HistoriqueFondController {

    private final TypeTransactionService typeTransactionService;
    private final HistoriqueFondService historiqueFondService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("historique", new HistoriqueFondRequest())
            .addAttribute("types", typeTransactionService.liste());

        return "pages/transaction/formulaire_depot_retrait";
    }

    @PostMapping("/ajouter")
    public String ajouter(HistoriqueFondRequest historique) throws MessagingException {
        historiqueFondService.creerHistoriqueFondTemporaire(historique, authenticationManager.safelyGetCurrentUser());
        return "redirect:/historique-fond/form";
    }

    @GetMapping("/valider")
    public String validerTransaction(@RequestParam("token") String token) {
        historiqueFondService.confirmerTransaction(token);
        return "redirect:/historique-fond/form";
    }

    @GetMapping("/utilisateur")
    public String transactionsUtilisateurCourant(Model model) {
        model.addAttribute("transactions", historiqueFondService.transactionProfil(authenticationManager.safelyGetCurrentUser()));

        return "pages/historique/historique_fond";
    }

}
