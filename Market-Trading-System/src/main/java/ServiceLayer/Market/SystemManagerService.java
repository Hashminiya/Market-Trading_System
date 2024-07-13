package ServiceLayer.Market;

import API.InitCommand;
import DAL.PurchaseDTO;
import DomainLayer.Market.Purchase.IPurchaseFacade;
import DomainLayer.Market.Purchase.Purchase;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.SystemManager;
import DomainLayer.Market.Util.JwtService;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.User.UserService;
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

import javax.ws.rs.core.Response;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service("SystemManagerService")
public class SystemManagerService implements ISystemManagerService {
    private static final Logger logger = LogManager.getLogger(StoreManagementService.class);

    @Value("${logging.include-exception:false}")
    private boolean includeException;

    String USER_NOT_VALID = "Authentication failed";
    private static SystemManagerService instance;
    private IStoreFacade storeFacade;
    private IUserFacade userFacade;
    private IPurchaseFacade purchaseFacade;
    private JwtService jwtService;

    @Autowired
    private SystemManagerService(@Qualifier("purchaseController") IPurchaseFacade purchaseFacade,
                                 @Qualifier("StoreController") IStoreFacade storeFacade,
                                 @Qualifier("userController") IUserFacade userFacade) {
        //SystemManager systemManager = SystemManager.getInstance();
        this.purchaseFacade = purchaseFacade;
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.jwtService = new JwtService();
    }

    public void clear(){
        storeFacade.clear();
        userFacade.clear();
        purchaseFacade.clear();
        instance = null;
    }

    public static synchronized SystemManagerService getInstance(IPurchaseFacade purchaseFacade, IStoreFacade storeFacade, IUserFacade userFacade) {
        if (instance == null) {
            instance = new SystemManagerService(purchaseFacade, storeFacade, userFacade);
        }
        return instance;
    }
    public void setPurchaseFacade(IPurchaseFacade purchaseFacade) {
        this.purchaseFacade = purchaseFacade;
    }

    public void setStoreFacade(IStoreFacade storeFacade) {
        this.storeFacade = storeFacade;
    }

    public void setUserFacade(IUserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public ResponseEntity<?> init(String token) {
        return null;
    }

    @Override
    public ResponseEntity<?> viewMarketPurchaseHistory(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                List<Purchase> purchases = purchaseFacade.getPurchaseHistory(userName);
                List<PurchaseDTO> purchaseDTOs = purchases.stream()
                        .map(purchase -> new PurchaseDTO(
                                purchase.getPurchaseId(),
                                purchase.getPurchasedItemsList(),
                                purchase.getTotalAmount(),
                                purchase.getUserId(),
                                purchase.getPurchaseDate()))
                        .collect(Collectors.toList());

                logger.info("View market purchase history by: {}", userName);
                return ResponseEntity.ok(purchaseDTOs);
            } else {
                logger.warn("Invalid token for creating store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        } catch (CannotCreateTransactionException | DataAccessException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to view market purchase history due to database connectivity issue\nError message: %s", e.getMessage()));
        } catch (Exception exception) {
            logException("Exception in viewMarketPurchaseHistory", exception);
            return ResponseEntity.status(500).body(exception.getMessage());
        }
    }

    @Override
    @InitCommand(name = "closeStore")
    public ResponseEntity<?> closeStore(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.removeStore(userName,storeId);
                ResponseEntity<?> response = ResponseEntity.ok().build();
                logger.info("remove store {} by: {}", storeId, userName);
                return response;
            }
            else {
                logger.warn("Invalid token for removing store: {}", token);
                return ResponseEntity.status(401).body(USER_NOT_VALID);
            }
        }
        catch (CannotCreateTransactionException | DataAccessException | SocketTimeoutException | SQLException e) {
            logException("Database connection error: ", e);
            return ResponseEntity.status(500).body(String.format("Database connection error: Unable to close store due to database connectivity issue\nError message: %s", e.getMessage()));
        }
        catch (Exception exception){
            logException("Exception in closeStore", exception);
            return ResponseEntity.status(500).body(exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> closeMarket(String token) {
        return null;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private void logException(String message, Exception e) {
        if (includeException) {
            logger.error(message,e);
        } else {
            logger.error("{}, {}", message, e.getMessage());
        }
    }
}
