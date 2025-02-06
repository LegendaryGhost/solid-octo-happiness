package mg.itu.cryptomonnaie.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.entity.HistoriqueFond;
import mg.itu.cryptomonnaie.entity.Profil;
import mg.itu.cryptomonnaie.request.HistoriqueFondRequest;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueFondService;
import mg.itu.cryptomonnaie.service.TypeTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    // A faire: tester ces methodes sur postman
    @PostMapping("/ajouter/en-attente")
    public String ajouterEnAttente(HistoriqueFondRequest historique) {
        Profil profil = authenticationManager.safelyGetCurrentUser();
        historiqueFondService.creerHistoriqueFondEnAttente(historique, profil);
        return "redirect:/historique-fond/form";
    }

    @GetMapping("/liste/en-attente")
    public String listeEnAttente(Model model) {
        List<HistoriqueFond> historiqueFonds = historiqueFondService.listeTransactionFondEnAttente();
        model.addAttribute("historiqueFonds", historiqueFonds);
        // a faire page pour rediriger ceci!!
        return "redirect:/historique-fond/form";
    }

    @GetMapping("/valider/transaction-fond/{id}")
    public String validerTransactionFond(@PathVariable("id") Long id, Model model) {
        HistoriqueFond historiqueFond = historiqueFondService.validerTransactionFond(id);
        return listeEnAttente(model);
    }

    @GetMapping("/refuser/transaction-fond/{id}")
    public String refuserTransactionFond(@PathVariable("id") Long id, Model model) {
        HistoriqueFond historiqueFond = historiqueFondService.refuserTransactionFond(id);
        return listeEnAttente(model);
    }

    // Taloha
    @GetMapping("/valider")
    public String validerTransaction(@RequestParam("token") String token) {
        historiqueFondService.confirmerTransaction(token);
        return "redirect:/historique-fond/form";
    }

    @GetMapping("/utilisateur")
    public String transactionsUtilisateurCourant(Model model) {
        model.addAttribute("transactions",
                historiqueFondService.transactionProfil(authenticationManager.safelyGetCurrentUser()));

        return "pages/historique/historique_fond";
    }

}
