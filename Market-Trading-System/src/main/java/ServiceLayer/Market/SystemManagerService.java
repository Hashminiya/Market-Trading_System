package ServiceLayer.Market;

import DomainLayer.Market.Purchase.IPurchaseFacade;
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
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
public class SystemManagerService implements ISystemManagerService {
    private static final Logger logger = LogManager.getLogger(StoreManagementService.class);
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
    public Response init(String token) {
        return null;
    }

    @Override
    public Response viewMarketPurchaseHistory(String token) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                Response response = Response.ok(purchaseFacade.getPurchaseHistory(userName)).build();
                logger.info("View market purchase history by: {}", userName);
                return response;
            }
            else {
                logger.warn("Invalid token for creating store: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        }
        catch (Exception exception){
            return Response.status(500).entity(exception.getMessage()).build();
        }
    }

    @Override
    public Response closeStore(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userFacade.loadUserByUsername(userName))) {
                storeFacade.removeStore(userName,storeId);
                Response response = Response.ok().build();
                logger.info("remove store {} by: {}", storeId, userName);
                return response;
            }
            else {
                logger.warn("Invalid token for removing store: {}", token);
                return Response.status(500).entity(USER_NOT_VALID).build();
            }
        }
        catch (Exception exception){
            return Response.status(500).entity(exception.getMessage()).build();
        }
    }

    @Override
    public Response closeMarket(String token) {
        return null;
    }

    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }
}
