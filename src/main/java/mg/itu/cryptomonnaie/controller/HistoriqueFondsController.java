package mg.itu.cryptomonnaie.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.request.HistoriqueFondsRequest;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.HistoriqueFondsService;
import mg.itu.cryptomonnaie.service.TypeOperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/historique-fonds")
public class HistoriqueFondsController {
    private final HistoriqueFondsService historiqueFondsService;
    private final TypeOperationService  typeOperationService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/creation")
    public String form(Model model) {
        model.addAttribute("hf", new HistoriqueFondsRequest())
            .addAttribute("typesOperation", typeOperationService.getAll());

        return "pages/transaction/formulaire_depot_retrait";
    }

    @PostMapping("/creation")
    public String ajouter(
        @ModelAttribute("hf") HistoriqueFondsRequest historiqueFondsRequest
    ) throws MessagingException {
        historiqueFondsService.creerHistoriqueFondsTemporaire(historiqueFondsRequest, authenticationManager.safelyGetCurrentUser());

        return "redirect:/historique-fond/form";
    }

    @GetMapping("/valider")
    public String validerTransaction(@RequestParam("token") String token) {
        historiqueFondsService.confirmerOperation(token);

        return "redirect:/historique-fond/form";
    }

    @GetMapping("/utilisateur")
    public String transactionsUtilisateurCourant(Model model) {
        model.addAttribute("transactions", historiqueFondsService.transactionProfil(authenticationManager.safelyGetCurrentUser()));

        return "pages/historique/historique_fond";
    }
}
