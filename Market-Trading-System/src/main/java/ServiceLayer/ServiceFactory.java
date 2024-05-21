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

    public static ServiceFactory getServiceFactory(){
        if(serviceFactoryInstace == null) {
            serviceFactoryInstace = new ServiceFactory();
        }
        return serviceFactoryInstace;
    }

    public static void initFactory() {
        // Create singleton instances of controllers
        userFacadeInstance = IUserFacade.getInstance(new InMemoryRepository<String, User>());
        storeFacadeInstance = IStoreFacade.getInstance(new InMemoryRepository<Long, Store>(), purchaseFacadeInstance, userFacadeInstance);

        // Create services
        storeManagementServiceInstance = StoreManagementService.getInstance(storeFacadeInstance);
        storeBuyerServiceInstance = StoreBuyerService.getInstance(storeFacadeInstance);
        userServiceInstance = UserService.getInstance(userFacadeInstance);

        // TODO : Ask Yagil about this type of implementation
        paymentServiceInstance = PaymentServiceImpl.getInstance();
        supplyServiceInstance = SupplyServiceImpl.getInstance();

        paymentServiceProxyInstance = PaymentServiceProxy.getInstance(paymentServiceInstance);
        supplyServiceProxyInstance =  SupplyServiceProxy.getInstance(supplyServiceInstance);
        purchaseFacadeInstance = IPurchaseFacade.getInstance(paymentServiceProxyInstance, supplyServiceProxyInstance);
    }

    // Factory methods for singleton instances of controllers
    public static IStoreFacade getStoreFacade() {
        return storeFacadeInstance;
    }

    public static IUserFacade getUserFacade() {
        return userFacadeInstance;
    }

    // Factory methods for services
    public static StoreManagementService getStoreManagementService() {
        return storeManagementServiceInstance;
    }

    public static StoreBuyerService getStoreBuyerService() {
        return storeBuyerServiceInstance;
    }

    public static UserService getUserService() {
        return userServiceInstance;
    }

    public static PaymentServiceImpl paymentService(){
        return paymentServiceInstance;
    }

}
