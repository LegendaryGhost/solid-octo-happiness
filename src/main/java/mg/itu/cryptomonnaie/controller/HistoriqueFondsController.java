package mg.itu.cryptomonnaie.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.enums.TypeOperation;
import mg.itu.cryptomonnaie.request.OperationRequest;
import mg.itu.cryptomonnaie.security.AuthenticationManager;
import mg.itu.cryptomonnaie.service.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/historique-fonds")
public class HistoriqueFondsController {
    private final OperationService operationService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/creation")
    public String form(Model model) {
        model.addAttribute("hf", new OperationRequest())
            .addAttribute("typesOperation", TypeOperation.values());

        return "pages/transaction/formulaire_depot_retrait";
    }

    @PostMapping("/creation")
    public String ajouter(
        @ModelAttribute("hf") OperationRequest operationRequest
    ) throws MessagingException {
        operationService.creerHistoriqueFondsTemporaire(operationRequest, authenticationManager.safelyGetCurrentUser());

        return "redirect:/historique-fond/form";
    }

    @GetMapping("/valider")
    public String validerTransaction(
        @RequestParam("token") String token
    ) {
        operationService.confirmerOperation(authenticationManager.safelyGetCurrentUser(), token);

        return "redirect:/historique-fond/form";
    }

    @GetMapping("/utilisateur")
    public String transactionsUtilisateurCourant(Model model) {
        model.addAttribute("transactions", operationService.transactionProfil(authenticationManager.safelyGetCurrentUser()));

        return "pages/historique/historique_fond";
    }
}
