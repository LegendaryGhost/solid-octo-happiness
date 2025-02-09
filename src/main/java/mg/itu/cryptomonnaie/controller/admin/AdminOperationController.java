package mg.itu.cryptomonnaie.controller.admin;

import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.service.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/operations")
public class AdminOperationController {

    private final OperationService operationService;

    @GetMapping
    public String index(Model model) {
	model.addAttribute("operations", operationService.getAllEnAttente());

	return "admin/operations";
    }

}
