package ServiceLayer.Notifications;

import DomainLayer.Market.Notifications.INotificationFacade;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.Util.JwtService;
import ServiceLayer.Store.StoreManagementService;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("NotificationService")
public class NotificationService implements INotificationService {
    private static final Logger logger = LogManager.getLogger(StoreManagementService.class);

    @Value("${logging.include-exception:false}")
    private boolean includeException;

    String USER_NOT_VALID = "Authentication failed";
    INotificationFacade notificationFacade;
    private JwtService jwtService;
    private IUserFacade userFacade;

    @Autowired
    public NotificationService(@Qualifier("NotificationController") INotificationFacade notificationFacade,
                               @Qualifier("userController") IUserFacade userFacade){
        this.notificationFacade = notificationFacade;
        this.jwtService = new JwtService();
        this.userFacade = userFacade;
    }
    @Override
    public void save(String recipient, String content, Date date) {
        notificationFacade.save(recipient, content, date);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getMessages(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                ResponseEntity<List<String>> response = ResponseEntity.status(200).body(notificationFacade.getMessages(userName));
                logger.info("user {} received messages", userName);
                return response;
            } else {
                logger.warn("Invalid token for get messages: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to get messages due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error getting messages store", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> clear(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                ResponseEntity<List<String>> response = ResponseEntity.status(200).body(notificationFacade.clear(userName));
                logger.info("user {} received messages", userName);
                return response;
            } else {
                logger.warn("Invalid token for get messages: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to clear messages due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception ex) {
            logException("Error getting messages store", ex);
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    private void logException(String message, Exception e) {
        if (includeException) {
            logger.error(message,e);
        } else {
            logger.error("{}, {}", message, e.getMessage());
        }
    }
}
