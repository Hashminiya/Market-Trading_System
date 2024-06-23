package API.controller;

import API.WebSocketHandler;
import DomainLayer.Market.Notification.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class NotifiactionController {
    @Autowired
    private static WebSocketHandler webSocketServer;
    @Autowired
    public  NotifiactionController(WebSocketHandler webSocketServer) {
        this.webSocketServer =  webSocketServer;
    }

    @EventListener
    public void processMessage(Event event){
        String userId = (String) event.getUserIds().toArray()[0];
        webSocketServer.handleMessage(userId, new TextMessage(event.getMessage()));

    }
}
