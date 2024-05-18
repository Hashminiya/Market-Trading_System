package ServiceLayer;

import ServiceLayer.Store.StoreManagementService;
import ServiceLayer.Store.StoreBuyerService;
import ServiceLayer.User.UserService;
import DomainLayer.Market.Store.StoreController;
import DomainLayer.Market.User.UserController;

public class ServiceFactory {
    private static StoreController storeControllerInstance;
    private static UserController userControllerInstance;

    // Factory methods for singleton instances of controllers
    public static StoreController getStoreController() {
        if (storeControllerInstance == null) {
            storeControllerInstance = StoreController.getInstance();
        }
        return storeControllerInstance;
    }

    public static UserController getUserController() {
        if (userControllerInstance == null) {
            userControllerInstance = UserController.getInstance();
        }
        return userControllerInstance;
    }

    // Factory methods for services
    public static StoreManagementService createStoreManagementService() {
        return new StoreManagementService(getStoreController());
    }

    public static StoreBuyerService createStoreBuyerService() {
        return new StoreBuyerService(getStoreController());
    }

    public static UserService createUserService() {
        return new UserService(getUserController());
    }
}
