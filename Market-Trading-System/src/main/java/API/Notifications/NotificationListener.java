package API.Notifications;

import DomainLayer.Market.Notifications.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class NotificationListener {
    @Autowired
    private WebSocketHandler webSocketServer;
    @Autowired
    public NotificationListener(WebSocketHandler webSocketServer) {
        this.webSocketServer =  webSocketServer;
    }

    @EventListener
    public void processMessage(Event event){
        String userId = (String) event.getUserIds().toArray()[0];
        webSocketServer.handleMessage(userId, new TextMessage(event.getMessage()));

    }
}
