package UnitTests;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class UserFacadeUT {

    @Mock
    private UserController userControllerMock;

    @InjectMocks
    private IUserFacade userFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes annotated fields
        userFacade = new UserController();
    }

    @Test
    void testCreateGuestSession() {
        userFacade.createGuestSession();
        verify(userControllerMock).createGuestSession();
    }

    @Test
    void testTerminateGuestSession() {
        userFacade.terminateGuestSession();
        verify(userControllerMock).terminateGuestSession();
    }

    @Test
    void testRegister() {
        String userName = "testUser";
        String password = "testPassword";

        userFacade.register(userName, password);
        verify(userControllerMock).register(userName, password);
    }

    @Test
    void testLogin() {
        String userName = "testUser";
        String password = "testPassword";

        userFacade.login(userName, password);
        verify(userControllerMock).login(userName, password);
    }

    @Test
    void testLogout() {
        String userName = "testUser";

        userFacade.logout(userName);
        verify(userControllerMock).logout(userName);
    }

    @Test
    void testViewShoppingCart() {
        String token = "testToken";

        userFacade.viewShoppingCart(token);
        verify(userControllerMock).viewShoppingCart(token);
    }

    @Test
    void testModifyShoppingCart() {
        String token = "testToken";

        userFacade.modifyShoppingCart(token);
        verify(userControllerMock).modifyShoppingCart(token);
    }

    @Test
    void testCheckoutShoppingCart() {
        String token = "testToken";

        userFacade.checkoutShoppingCart(token);
        verify(userControllerMock).checkoutShoppingCart(token);
    }
}
