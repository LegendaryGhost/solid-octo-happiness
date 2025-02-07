package mg.itu.cryptomonnaie.controller;

import mg.itu.cryptomonnaie.service.FCMService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private final FCMService fcmService;

    public NotificationController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send-notification")
    public String sendNotification(@RequestParam String token, @RequestParam String title, @RequestParam String body) {
        fcmService.sendNotification(token, title, body);
        return "Notification sent";
    }
}