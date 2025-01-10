package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerValidationHistoFondEmail(String recipientEmail, String token) {
	String url = "http://localhost:8080/historique-fond/valider?token=" + token;

	SimpleMailMessage message = new SimpleMailMessage();
	message.setTo(recipientEmail);
	message.setSubject("Validation de votre transaction de fond");

	String htmlContent = "<html><body>";
	htmlContent += "<h2>Veuillez valider votre transaction !</h2>";
	htmlContent += "<p>Cliquez sur le lien ci-dessous pour valider votre transaction :</p>";
	htmlContent += "<a href='" + url + "' target='_blank'>Valider</a>";
	htmlContent += "</body></html>";

	message.setText(htmlContent);

	mailSender.send(message);
    }
}
