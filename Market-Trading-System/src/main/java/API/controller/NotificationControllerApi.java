package API.controller;

import ServiceLayer.Market.ISystemManagerService;
import ServiceLayer.Notifications.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationControllerApi {
    private final INotificationService notificationService;

    @Autowired
    public NotificationControllerApi(@Qualifier("NotificationService") INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/viewMessages")
    public ResponseEntity<?> viewMessages(@RequestParam String token){
        return notificationService.getMessages(token);
    }


}
