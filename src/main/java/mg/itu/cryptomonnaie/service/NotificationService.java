package mg.itu.cryptomonnaie.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mg.itu.cryptomonnaie.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final RestTemplate restTemplate;
    private final ParameterizedTypeReference<Map<String, Object>> mapTypeReference;

    @Value("${expopush.url}")
    private String expoPushUrl;

    public boolean envoyerNotificationPush(
        final String expoPushToken, final String title, final String body
    ) {
        HttpHeaders httpHeaders = Utils.createJsonContentTypeHttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(expoPushUrl, HttpMethod.POST,
                new HttpEntity<>(Map.of(
                    "to", expoPushToken,
                    "title", title,
                    "body", body
                ), httpHeaders), mapTypeReference);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.trace("Notification correctement envoyée à \"{}\"", expoPushToken);
                return true;
            } else return false;
        } catch (Exception e) {
            log.error("Une erreur s'est produite lors de l'envoi de la notification vers \"{}\"", expoPushToken, e);
            return false;
        }
    }
}
