package DomainLayer.Market.User;

import ServiceLayer.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class SystemManager extends User{
    private static int USER_ID = 1;
    private static String USER_NAME = "SystemManager";
    private static int USER_AGE = 35;
    private static List<Istate> USER_STATES = new ArrayList<>(); // Assuming Istate is the correct type
    private static boolean LOGGED_IN = true; // Assuming the SystemManager is logged in by default
    private static ShoppingCart SHOPPING_CART = new ShoppingCart(); // Assuming ShoppingCart is the correct type

    private static SystemManager systemManager;

    private SystemManager(int userID, String userName, int userAge, List<Istate> userStates, boolean loggedIn, ShoppingCart shoppingCart){
        super(userID, userName, userAge, userStates, loggedIn, shoppingCart);
    }

    public static synchronized SystemManager getInstance(){
        if(systemManager == null){
            systemManager = new SystemManager(USER_ID, USER_NAME, USER_AGE , USER_STATES, LOGGED_IN, SHOPPING_CART);
        }
        return systemManager;
    }

    public void InitializeTradingSystem(){
        ServiceFactory.initFactory();
    }

    public void AddConnectionWithExternalServices(){

    }

    public void ChangeConnectionWithExternalServices(){

    }
}
