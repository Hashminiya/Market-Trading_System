package API.Notifications;

import API.Utils.SpringContext;
import DomainLayer.Market.Notifications.Event;
import DomainLayer.Market.Notifications.Publisher;
import ServiceLayer.Notifications.NotificationService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private static WebSocketHandler instance;

    private static final Map<String, WebSocketSession> usersSessions = new ConcurrentHashMap<>();
    private WebSocketHandler(){

    }
    public static synchronized WebSocketHandler getInstance(){
        if(instance == null){
            instance = new WebSocketHandler();
        }
        return instance;
    }
    public void handleMessage(String userName, WebSocketMessage<?> message) {
        String receivedMessage = (String) message.getPayload();
        WebSocketSession session = usersSessions.get(userName);
        if (session == null){
            saveMessage(userName,receivedMessage);
        }
        else {
            try {
                session.sendMessage(new TextMessage("Received: " + receivedMessage));
            } catch (IOException e) {
                //TODO write to log
            }
        }
    }

    private void saveMessage(String userName, String receivedMessage) {
        ((NotificationService) SpringContext.getBean("NotificationService")).save(userName, receivedMessage, new Date());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = (String) session.getAttributes().get("username");
        if (userName != null) {
            usersSessions.put(userName, session);
        }
        Event event = new Event(new Object(), "connect message from server!!!",new HashSet<>(Arrays.asList(userName)));
        Publisher publisher = (Publisher) SpringContext.getBean("Publisher");
        publisher.publish(event);
    }
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userName = (String) session.getAttributes().get("username");
        if (userName != null) {
            usersSessions.remove(userName);
        }
    }
}
