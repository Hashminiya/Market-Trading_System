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
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;

import javax.imageio.stream.ImageInputStreamImpl;

public class ServiceFactory {
    // Controllers
    private static IStoreFacade storeFacadeInstance;
    private static IUserFacade userFacadeInstance;
    private static IPurchaseFacade purchaseFacadeInstance;

    // Services
    private static StoreManagementService storeManagementServiceInstance;
    private static StoreBuyerService storeBuyerServiceInstance;
    private static UserService userServiceInstance;
    private static PaymentServiceImpl paymentServiceInstance;
    private static SupplyServiceImpl supplyServiceInstance;
    private static PaymentServiceProxy paymentServiceProxyInstance;
    private static SupplyServiceProxy supplyServiceProxyInstance;

    // Factory instance
    private static ServiceFactory serviceFactoryInstance;

    private ServiceFactory(){
        InMemoryRepository<String, User> user_repo = new InMemoryRepository<String,User>();
        userFacadeInstance = IUserFacade.getInstance(user_repo);
        try {
            loadUserRepo();
        }
        catch (Exception exception){
            // Do nothing for now..
        }
    }

    public static synchronized ServiceFactory getServiceFactory(){
        if(serviceFactoryInstance == null) {
            serviceFactoryInstance = new ServiceFactory();
        }
        return serviceFactoryInstance;
    }

    public void initFactory() {
        // Init payment and supply services and proxy
        paymentServiceInstance = PaymentServiceImpl.getInstance();
        supplyServiceInstance = SupplyServiceImpl.getInstance();
        paymentServiceProxyInstance = PaymentServiceProxy.getInstance(paymentServiceInstance);
        supplyServiceProxyInstance =  SupplyServiceProxy.getInstance(supplyServiceInstance);

        // Init Facades
        purchaseFacadeInstance = IPurchaseFacade.getInstance(new InMemoryRepository<Long, Purchase>(), paymentServiceProxyInstance,supplyServiceProxyInstance);
        storeFacadeInstance = IStoreFacade.getInstance(new InMemoryRepository<Long, Store>());
        userFacadeInstance.setStoreFacade(storeFacadeInstance);
        userFacadeInstance.setPurchaseFacade(purchaseFacadeInstance);
        storeFacadeInstance.setUserFacade(userFacadeInstance);
        storeFacadeInstance.setPurchaseFacade(purchaseFacadeInstance);

        // Init Services
        storeManagementServiceInstance = StoreManagementService.getInstance(storeFacadeInstance);
        storeBuyerServiceInstance = StoreBuyerService.getInstance(storeFacadeInstance);
        userServiceInstance = UserService.getInstance(userFacadeInstance);
    }

    private static void loadUserRepo() throws Exception{
        SystemManager systemManager = SystemManager.getInstance();
        userFacadeInstance.register(systemManager.getUserName(), systemManager.getPassword(), systemManager.getUserAge());
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

}
