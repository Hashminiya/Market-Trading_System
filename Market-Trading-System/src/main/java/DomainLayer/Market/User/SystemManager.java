package DomainLayer.Market.User;

import DomainLayer.Market.ShoppingBasket;
import DomainLayer.Market.Util.InMemoryRepository;
import ServiceLayer.ServiceFactory;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("SystemManager")
public class SystemManager extends User{
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";
    private static final int USER_AGE = 25;
    private static final boolean LOGGED_IN = false;
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart(new InMemoryRepository<Long, ShoppingBasket>()); // Assuming ShoppingCart is the correct type
    private static final Istate STATE = new Registered();

    private static SystemManager systemManager;

    @Autowired
    private SystemManager(@Value("${systemManager.username}") String userName, @Value("${systemManager.password}") String password, @Value("${systemManager.userAge}") int userAge, @Qualifier("registered") Istate state, @Value("${systemManager.loggedIn}") boolean loggedIn, ShoppingCart shoppingCart){
        super(userName, password, userAge, state, loggedIn, shoppingCart);
    }

    public static synchronized SystemManager getInstance(){
        if(systemManager == null){
            systemManager = new SystemManager(USER_NAME, PASSWORD, USER_AGE, STATE, LOGGED_IN, SHOPPING_CART);
        }
        return systemManager;
    }

    public void AddConnectionWithExternalServices(){

    }

    public void ChangeConnectionWithExternalServices(){

    }
}
