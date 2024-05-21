package ServiceLayer;

import DomainLayer.Market.Purchase.*;
import DomainLayer.Market.Purchase.OutServices.PaymentServiceImpl;
import DomainLayer.Market.Purchase.OutServices.SupplyServiceImpl;
import DomainLayer.Market.Store.IStoreFacade;
import DomainLayer.Market.Store.Store;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.User;
import DomainLayer.Market.Util.IRepository;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;

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

    private static ServiceFactory serviceFactoryInstace;

    private ServiceFactory(){
        initFactory();
    }

    public static synchronized ServiceFactory getServiceFactory(){
        if(serviceFactoryInstace == null) {
            serviceFactoryInstace = new ServiceFactory();
        }
        return serviceFactoryInstace;
    }

    public static void initFactory() {

        // TODO : Ask about this type of implementation
        paymentServiceInstance = PaymentServiceImpl.getInstance();
        supplyServiceInstance = SupplyServiceImpl.getInstance();
        paymentServiceProxyInstance = PaymentServiceProxy.getInstance(paymentServiceInstance);

        supplyServiceProxyInstance =  SupplyServiceProxy.getInstance(supplyServiceInstance);
        purchaseFacadeInstance = IPurchaseFacade.getInstance(paymentServiceProxyInstance, paymentServiceInstance, supplyServiceProxyInstance, supplyServiceInstance);
        //

        userFacadeInstance = IUserFacade.getInstance(new InMemoryRepository<String, User>());
        storeFacadeInstance = IStoreFacade.getInstance(new InMemoryRepository<Long, Store>());
        userFacadeInstance.setStoreFacade(storeFacadeInstance);
        storeFacadeInstance.setUserFacade(userFacadeInstance);
        storeFacadeInstance.setPurchaseFacade(purchaseFacadeInstance);

        storeManagementServiceInstance = StoreManagementService.getInstance(storeFacadeInstance);
        storeBuyerServiceInstance = StoreBuyerService.getInstance(storeFacadeInstance);
        userServiceInstance = UserService.getInstance(userFacadeInstance);
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
