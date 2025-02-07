package mg.itu.cryptomonnaie.controller;


import mg.itu.cryptomonnaie.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Autowired
    private  PushNotificationService pushNotificationService;

    @PostMapping("/send-notification")
    public String sendNotification(@RequestParam String token, @RequestParam String title, @RequestParam String body) {
        pushNotificationService.sendPushNotification(token, title, body);
//        fcmService.sendNotification(token, title, body);
        return "Notification sent";
    }
}