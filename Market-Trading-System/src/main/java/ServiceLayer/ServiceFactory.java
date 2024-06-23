package ServiceLayer;

import DomainLayer.Market.Purchase.*;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.SystemManager;
import DomainLayer.Market.User.User;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import DomainLayer.Market.Util.JwtService;
import DomainLayer.Repositories.InMemoryUserRepository;
import ServiceLayer.Market.ISystemManagerService;
import ServiceLayer.Market.SystemManagerService;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.ws.rs.core.Response;

public class ServiceFactory {
    private boolean systemAvailable;
    // Controllers
    private static IStoreFacade storeFacadeInstance;
    private static IUserFacade userFacadeInstance;
    private static IPurchaseFacade purchaseFacadeInstance;

    // Services
    private static SystemManagerService systemManagerService;
    private static StoreManagementService storeManagementServiceInstance;
    private static StoreBuyerService storeBuyerServiceInstance;
    private static UserService userServiceInstance;
    private static PaymentServiceImpl paymentServiceInstance;
    private static SupplyServiceImpl supplyServiceInstance;
    private static PaymentServiceProxy paymentServiceProxyInstance;
    private static SupplyServiceProxy supplyServiceProxyInstance;

    // Factory instance
    private static ServiceFactory serviceFactoryInstance;
    private JwtService jwtService;

    private ServiceFactory(){
        systemAvailable = false;
        userFacadeInstance = IUserFacade.getInstance(new InMemoryUserRepository(), SystemManager.getInstance(), storeFacadeInstance, purchaseFacadeInstance);
        userServiceInstance = UserService.getInstance(userFacadeInstance);
        jwtService = new JwtService();
        userServiceInstance.setJwtService(jwtService);
        loadUserRepo();
    }

    private Response loadUserRepo() {
        try {
            SystemManager systemManager = SystemManager.getInstance();
            userServiceInstance.register(systemManager.getUserName(),systemManager.getPassword(),systemManager.getUserAge());
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    public static synchronized ServiceFactory getServiceFactory(){
        if(serviceFactoryInstance == null) {
            serviceFactoryInstance = new ServiceFactory();
        }
        return serviceFactoryInstance;
    }

    public Response initFactory(String token) {
        if(validAdmin(token)) {
            initExternalServices();
            initFacades();
            initServices();
            systemAvailable = true;
            return Response.ok().build();
        }
        else{
            return Response.status(500).entity("User os not authorized to init the system. Make sure your loged in with admin user").build();
        }

    }

    private boolean validAdmin(String token) {
        String userName = jwtService.extractUsername(token);
        if(jwtService.isValid(token, userFacadeInstance.loadUserByUsername(userName))) {
            return userFacadeInstance.isAdmin(userName);
        }
        return false;
    }

    private void initExternalServices(){
        paymentServiceInstance = PaymentServiceImpl.getInstance();
        supplyServiceInstance = SupplyServiceImpl.getInstance();
        paymentServiceProxyInstance = PaymentServiceProxy.getInstance(paymentServiceInstance);
        supplyServiceProxyInstance =  SupplyServiceProxy.getInstance(supplyServiceInstance);
    }
    private void initFacades() {
        purchaseFacadeInstance = IPurchaseFacade.getInstance(new InMemoryRepository<Long, Purchase>(), paymentServiceProxyInstance,supplyServiceProxyInstance);
        purchaseFacadeInstance.setUserFacade(userFacadeInstance);
        storeFacadeInstance = IStoreFacade.getInstance(new InMemoryRepository<Long, Store>(), userFacadeInstance,purchaseFacadeInstance);
        userFacadeInstance.setStoreFacade(storeFacadeInstance);
        userFacadeInstance.setPurchaseFacade(purchaseFacadeInstance);
        storeFacadeInstance.setUserFacade(userFacadeInstance);
        storeFacadeInstance.setPurchaseFacade(purchaseFacadeInstance);
    }

    private void initServices(){
        systemManagerService = SystemManagerService.getInstance(purchaseFacadeInstance, storeFacadeInstance, userFacadeInstance);
        systemManagerService.setPurchaseFacade(purchaseFacadeInstance);
        systemManagerService.setStoreFacade(storeFacadeInstance);
        systemManagerService.setUserFacade(userFacadeInstance);
        systemManagerService.setJwtService(new JwtService());

        storeManagementServiceInstance = StoreManagementService.getInstance(storeFacadeInstance,userFacadeInstance);
        storeManagementServiceInstance.setJwtService(new JwtService());
        storeManagementServiceInstance.setUserFacade(userFacadeInstance);

        storeBuyerServiceInstance = StoreBuyerService.getInstance(storeFacadeInstance);

        userServiceInstance = UserService.getInstance(userFacadeInstance);
        userServiceInstance.setJwtService(new JwtService());
    }

    public ISystemManagerService getSystemManagerService() {
        return systemManagerService;
    }

    public IStoreFacade getStoreFacade() {
        return storeFacadeInstance;
    }

    public IUserFacade getUserFacade() {
        return userFacadeInstance;
    }

    public IPurchaseFacade getPurchaseFacade(){
        return purchaseFacadeInstance;
    }

    // Factory methods for services
    public StoreManagementService getStoreManagementService() {
        return storeManagementServiceInstance;
    }

    public StoreBuyerService getStoreBuyerService() {
        return storeBuyerServiceInstance;
    }

    public UserService getUserService() {
        return userServiceInstance;
    }

    public PaymentServiceImpl paymentService(){
        return paymentServiceInstance;
    }

    public SupplyServiceImpl getSupplyService(){
        return supplyServiceInstance;
    }

    public PaymentServiceProxy getPaymentServiceProxy(){
        return paymentServiceProxyInstance;
    }

    public SupplyServiceProxy getSupplyServiceProxy(){
        return supplyServiceProxyInstance;
    }

    public boolean systemAvailable(){
        return systemAvailable;
    }

    public void clear(){
        systemAvailable= false;
        // Controllers
        storeFacadeInstance = null;
        userFacadeInstance = null;
        purchaseFacadeInstance = null;

        // Services
        systemManagerService.clear();
        storeManagementServiceInstance.clear();
        storeBuyerServiceInstance.clear();
        userServiceInstance.clear();
        paymentServiceInstance = null;
        supplyServiceInstance = null;
        paymentServiceProxyInstance = null;
        supplyServiceProxyInstance = null;

        // Factory instance
        serviceFactoryInstance = null;
        jwtService = null;
    }

    public void setPaymentServiceInstance(PaymentServiceImpl paymentServiceInstance) {
        ServiceFactory.paymentServiceInstance = paymentServiceInstance;
    }
}
