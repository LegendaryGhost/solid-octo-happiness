package mg.itu.cryptomonnaie.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class NotificationService {
    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void sendPushNotification(String expoPushToken, String title, String body) {
        RestTemplate restTemplate = new RestTemplate();

        // Créer le corps de la requête
        String message = String.format("{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\"}",
                expoPushToken, title, body);

        // Définir les en-têtes de la requête
        HttpHeaders headers = new HttpHeaders();
        headers.put("Accept", Collections.singletonList("application/json"));
        headers.put("Content-Type", Collections.singletonList("application/json"));

        // Créer l'entité de la requête
        HttpEntity<String> entity = new HttpEntity<>(message, headers);

        // Envoyer la requête POST
        ResponseEntity<String> response = restTemplate.exchange(EXPO_PUSH_URL, HttpMethod.POST, entity, String.class);

        // Gérer la réponse
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Notification envoyée avec succès");
        } else {
            System.out.println("Erreur lors de l'envoi de la notification : " + response.getStatusCode());
        }
    }
}
