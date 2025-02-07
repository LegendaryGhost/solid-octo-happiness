package mg.itu.cryptomonnaie.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Deprecated
@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public void envoyerEmailValidationHistoFonds(String recipientEmail, String token) throws MessagingException {
        String url = "http://localhost:8080/historique-fond/valider?token=" + token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("Validation de votre transaction de fond");

        String htmlContent = "<html><body style='font-family: Arial, sans-serif;'>";
        htmlContent += "<div style='text-align: center; margin: 20px;'>";
        htmlContent += "<h2 style='color: #333;'>Veuillez valider votre transaction !</h2>";
        htmlContent += "<p style='font-size: 16px; color: #555;'>Cliquez sur le bouton ci-dessous pour valider votre transaction :</p>";
        htmlContent += "<a href='" + url + "' target='_blank' style='text-decoration: none; padding: 10px 20px; background-color: #007BFF; color: white; border-radius: 5px;'>Valider</a>";
        htmlContent += "</div>";
        htmlContent += "<footer style='margin-top: 20px; text-align: center; font-size: 12px; color: #888;'>Cet email est automatique, merci de ne pas répondre.</footer>";
        htmlContent += "</body></html>";

        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }
}
