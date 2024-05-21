package DomainLayer.Market.User;

import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class SystemManager extends User{
    private static final String USER_NAME = "SystemManager";
    private static final String PASSWORD = "SystemManagerPassword";
    private static final int USER_AGE = 35;
    private static final List<Istate> USER_STATES = new ArrayList<>(); // Assuming Istate is the correct type
    private static final boolean LOGGED_IN = false; // Assuming the SystemManager is logged in by default
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart(new InMemoryRepository<Long, ShoppingBasket>()); // Assuming ShoppingCart is the correct type
    private static final Istate STATE = new Registered();

    private static SystemManager systemManager;

    private SystemManager(String userName, String password, int userAge, Istate state, boolean loggedIn, ShoppingCart shoppingCart){
        super(userName, password, userAge, state, loggedIn, shoppingCart);
    }

    public static synchronized SystemManager getInstance(){
        if(systemManager == null){
            systemManager = new SystemManager(USER_NAME, PASSWORD, USER_AGE, STATE, LOGGED_IN, SHOPPING_CART);
        }
        return systemManager;
    }

    public void InitializeTradingSystem(){
        if(loggedIn) {
            ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();
            serviceFactory.initFactory();
        }
    }

    public void AddConnectionWithExternalServices(){

    }

    public void ChangeConnectionWithExternalServices(){

    }
}
