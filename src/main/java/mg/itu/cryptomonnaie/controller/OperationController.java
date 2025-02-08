package mg.itu.cryptomonnaie.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.enums.TypeOperation;
import mg.itu.cryptomonnaie.request.OperationRequest;
import mg.itu.cryptomonnaie.service.OperationService;
import mg.itu.cryptomonnaie.utils.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
@RequestMapping("/operation")
public class OperationController {
    private final OperationService operationService;

    @GetMapping("/demande-depot-retrait")
    public String formulaireDemandeDepotRetrait(Model model) {
        model.addAttribute("o", new OperationRequest())
            .addAttribute("typesOperation", TypeOperation.values());

        return "operation/formulaire_depot_retrait";
    }

    @PostMapping("/ajouter-en-attente")
    public String ajouterEnAttente(
        @Valid @ModelAttribute("o") OperationRequest request,
        RedirectAttributes redirectAttributes
    ) {
        operationService.creerOperationEnAttente(request, Facade.authenticationManager().safelyGetCurrentUser());
        redirectAttributes.addFlashAttribute("success", "Votre demande a été enregistrée");

        return "redirect:/operation/demande-depot-retrait";
    }

    @GetMapping("/historique-globale")
    public String historiqueGlobale(
        Model model,
        @RequestParam(required = false) LocalDateTime dateHeure
    ) {
        model.addAttribute("operations", operationService.getHistoriqueGlobale(dateHeure));
        return "operation/historique_globale";
    }

    @GetMapping("/utilisateur/{idUtilisateur}")
    public String historiqueParUtilisateur(@PathVariable Integer idUtilisateur, Model model) {
        model.addAttribute("operations", operationService.getAllByUtilisateur(idUtilisateur));

        return null;
    }
}
