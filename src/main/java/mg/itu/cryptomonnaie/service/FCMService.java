package mg.itu.cryptomonnaie.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;


@Service
public class FCMService {

    public void sendNotification(String deviceToken, String title, String body) {
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setToken(deviceToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Message sent: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
