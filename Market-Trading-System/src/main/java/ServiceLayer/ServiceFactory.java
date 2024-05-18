package ServiceLayer;

import DomainLayer.Market.Purchase.PurchaseController;
import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;

public class ServiceFactory {
    private static StoreController storeControllerInstance;
    private static UserController userControllerInstance;
    private static PurchaseController purchaseControllerInstance;
    private static StoreManagementService storeManagementServiceInstance;
    private static StoreBuyerService storeBuyerServiceInstance;
    private static UserService userServiceInstance;

    // Initialize method to create singleton instances of controllers and services
    public static void initFactory() {
        // Create singleton instances of controllers
        storeControllerInstance = StoreController.getInstance();
        userControllerInstance = UserController.getInstance();
//        purchaseControllerInstance = ...
        // Create services

        // TODO : Service.getInstance(conteroller); ...
        storeManagementServiceInstance = new StoreManagementService(storeControllerInstance);
        storeBuyerServiceInstance = new StoreBuyerService(storeControllerInstance);
        userServiceInstance = new UserService(userControllerInstance);
    }

    // Factory methods for singleton instances of controllers
    public static synchronized StoreController getStoreController() {
        return storeControllerInstance;
    }

    public static synchronized UserController getUserController() {
        return userControllerInstance;
    }

    // Factory methods for services
    public static synchronized StoreManagementService getStoreManagementService() {
        return storeManagementServiceInstance;
    }

    public static synchronized StoreBuyerService getStoreBuyerService() {
        return storeBuyerServiceInstance;
    }

    public static synchronized UserService getUserService() {
        return userServiceInstance;
    }
}
