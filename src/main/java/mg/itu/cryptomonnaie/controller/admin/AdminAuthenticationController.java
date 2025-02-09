package mg.itu.cryptomonnaie.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mg.itu.cryptomonnaie.entity.Admin;
import mg.itu.cryptomonnaie.service.AdminAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class AdminAuthenticationController {

    private final AdminAuthenticationService adminAuthenticationService;

    @GetMapping("/admin-auth")
    public String formulaireAuthentification(Model model) {
	Admin credentials = new Admin("phoenix-coin.admin@gmail.com", "admin1234");
	model.addAttribute("credentials", credentials);

	return "admin/connexion";
    }

    @PostMapping("/admin-auth")
    public String authentifier(@ModelAttribute Admin admin, HttpSession session) {
	if (adminAuthenticationService.authentifier(admin)) {
	    admin.setMotDePasse(null);
	    session.setAttribute("admin", admin);
	    return "redirect:/admin/operations";
	}

	return "redirect:/admin/connexion";
    }

    @GetMapping("/admin-logout")
    public String deconnexion(HttpSession session) {
	session.removeAttribute("admin");
	return "redirect:/admin-auth";
    }

}
