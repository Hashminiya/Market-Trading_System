package ServiceLayer.Store;

import DomainLayer.Market.Store.Discount;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.JwtService;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.ws.rs.core.Response;
import java.util.List;

public class StoreManagementService implements IStoreManagementService{
    String USER_NOT_VALID = "Authentication failed";
    private static StoreManagementService instance;
    private final IStoreFacade storeFacade;
    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    public StoreManagementService(IStoreFacade storeFacade){
        this.storeFacade = storeFacade;
        jwtService = new JwtService();
        userDetailsService = new InMemoryUserDetailsManager();
    }

    public static synchronized StoreManagementService getInstance(IStoreFacade storeFacade) {
        if (instance == null) {
            instance = new StoreManagementService(storeFacade);
        }
        return instance;
    }

    @Override
    public Response createStore(String founderToken, String storeName, String storeDescription, IRepository<Long, Discount> repository) {
        try {
            String userName = jwtService.extractUsername(founderToken);
            if(jwtService.isValid(founderToken, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.createStore(userName, storeName, storeDescription, repository);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response addItemToStore(String token, long storeId, String itemName, String description ,double itemPrice, int stockAmount, List<String> categories) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.addItemToStore(userName, storeId, itemName, itemPrice, stockAmount, description, categories);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response updateItem(String token, long storeId, long itemId, String newName, double newPrice, int newAmount) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))){
                storeFacade.updateItem(userName,storeId,itemId,newName,newPrice, newAmount);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }


    @Override
    public Response deleteItem(String token, long storeId, long itemId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.deleteItem(userName, storeId, itemId);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeStorePolicy(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))){
                storeFacade.changeStorePolicy(userName, storeId);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response changeDiscountType(String token, long storeId, String newType) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.changeDiscountType(userName ,storeId, newType);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response removeStore(String token, long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.removeStore(userName, storeId);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewManagmentInfo(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                return Response.ok().entity(storeFacade.viewStoreManagementInfo(token, storeId)).build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewInventory(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if (jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                return Response.ok().entity(storeFacade.viewInventoryByStoreOwner(userName, storeId)).build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response viewPurchasesHistory(String token, Long storeId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))){
            return Response.ok().entity(storeFacade.viewPurchaseHistory(userName, storeId)).build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Response assignStoreOwner(String token, long storeId, String newOwnerId) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.assignStoreOwner(userName, storeId, newOwnerId);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Override
    public Response assignStoreManager(String token, long storeId, String newManagerI, List<String> permissions) {
        try {
            String userName = jwtService.extractUsername(token);
            if(jwtService.isValid(token, userDetailsService.loadUserByUsername(userName))) {
                storeFacade.assignStoreManager(userName, storeId, newManagerI, permissions);
                return Response.ok().build();
            }
            else return Response.status(500).entity(USER_NOT_VALID).build();
        }
        catch (Exception ex){
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
