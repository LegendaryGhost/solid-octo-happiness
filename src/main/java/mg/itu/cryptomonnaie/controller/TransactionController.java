package mg.itu.cryptomonnaie.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.request.TransactionRequest;
import mg.itu.cryptomonnaie.service.TransactionService;
import mg.itu.cryptomonnaie.utils.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/creation")
    public String formulaireCreation(Model model) {
        model.addAttribute("t", new TransactionRequest())
            .addAttribute("typesTransaction", TypeTransaction.values());

        return null;
    }

    @PostMapping("/creation")
    public String creer(
        @Valid @ModelAttribute("t") TransactionRequest request,
        RedirectAttributes redirectAttributes
    ) {
        transactionService.save(request, Facade.authenticationManager().safelyGetCurrentUser());
        redirectAttributes.addFlashAttribute("success", "Transaction effectuée avec succès");

        return null;
    }

    @GetMapping("/historique-globale")
    public String historiqueGlobale(
        Model model,
        @RequestParam(required = false) Integer idCryptomonnaie,
        @RequestParam(required = false) Integer idUtilisateur
    ) {
        model.addAttribute("historiques", transactionService.getHistoriqueGlobale(idCryptomonnaie, idUtilisateur));
        return null;
    }

    @GetMapping("/resumes-historiques-transaction")
    public String resumesHistoriquesTransaction(Model model) {
        model.addAttribute("resumesHistoriquesTransaction",
            transactionService.getResumesHistoriquesTransactionGroupByUtilisateur());

        return null;
    }
}
