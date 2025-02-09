package mg.itu.cryptomonnaie.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/operations")
public class AdminOperationController {

    @GetMapping
    public String index() {
	return "operation/historique_globale";
    }

}
