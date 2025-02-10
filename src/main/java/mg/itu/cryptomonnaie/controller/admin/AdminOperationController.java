package mg.itu.cryptomonnaie.controller.admin;

import lombok.RequiredArgsConstructor;
import mg.itu.cryptomonnaie.service.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/operations")
public class AdminOperationController {
    private final OperationService operationService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("operations", operationService.getAllEnAttente());

        return "admin/operations";
    }

    @GetMapping("/{id}/accepter")
    public String accepter(@PathVariable Integer id) {
        operationService.accepter(id);
        return "redirect:/admin/operations";
    }

    @GetMapping("/{id}/refuser")
    public String refuser(@PathVariable Integer id) {
        operationService.refuser(id);
        return "redirect:/admin/operations";
    }

}
