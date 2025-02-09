package mg.itu.cryptomonnaie.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.enums.TypeTransaction;
import mg.itu.cryptomonnaie.projections.ResumeHistoriqueTransactionUtilisateur;
import mg.itu.cryptomonnaie.request.TransactionRequest;
import mg.itu.cryptomonnaie.service.CryptomonnaieService;
import mg.itu.cryptomonnaie.service.TransactionService;
import mg.itu.cryptomonnaie.service.UtilisateurService;
import mg.itu.cryptomonnaie.utils.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final CryptomonnaieService cryptomonnaieService;
    private final UtilisateurService utilisateurService;

    @GetMapping("/creation")
    public String formulaireCreation(Model model) {
        model.addAttribute("t", new TransactionRequest())
                .addAttribute("cryptomonnaies", cryptomonnaieService.getAll())
                .addAttribute("typesTransaction", TypeTransaction.values());

        return "transaction/formulaire_achat_vente";
    }

    @PostMapping("/creation")
    public String creer(
            @Valid @ModelAttribute("t") TransactionRequest request,
            RedirectAttributes redirectAttributes) {
        transactionService.save(request, Facade.authenticationManager().safelyGetCurrentUser());
        redirectAttributes.addFlashAttribute("success", "Transaction effectuée avec succès");

        return "redirect:/transaction/creation";
    }

    @GetMapping("/historique-globale")
    public String historiqueGlobale(
            Model model,
            @RequestParam(required = false) Integer idCryptomonnaie,
            @RequestParam(required = false) Integer idUtilisateur) {
        model.addAttribute("transactions", transactionService.getHistoriqueGlobale(idCryptomonnaie, idUtilisateur))
                .addAttribute("cryptomonnaies", cryptomonnaieService.getAll())
                .addAttribute("utilisateurs", utilisateurService.getAll());
        return "transaction/historique_globale";
    }

    @GetMapping("/resumes-historiques-transaction")
    public String resumesHistoriquesTransaction(Model model) {
        model.addAttribute("resumesHistoriquesTransaction",
                transactionService.getResumesHistoriquesTransactionGroupByUtilisateur());

        return null;
    }

    @GetMapping("/filtre")
    public String filtreGlobal(@RequestParam(name = "dateMax",required = false) LocalDateTime localDateTime , Model model)
    {
        List<ResumeHistoriqueTransactionUtilisateur>  resumesHistoriquesTransactionGroupByUtilisateur = transactionService.getResumesHistoriquesTransactionGroupByUtilisateur();
        model.addAttribute("historiqueTransaction",resumesHistoriquesTransactionGroupByUtilisateur);
        return "transaction/filtre_global";
    }
}
