package UnitTests.DomainLayer.User;

import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;

public class UserFacadeUT {

    final String USER_NAME = "USER_NAME";
    final String TOKEN = "TOKEN";

//    @Mock
//    private IRepository<Long, ShoppingCart> carts;

//    @Mock
//    private IRepository<Long, User> users;

    @InjectMocks
    private IUserFacade userFacade = new UserController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes annotated fields
    }

    @Test
    void testRegister() {
//        // Arrange
//        String password = "newPassword";
//
//        // Mock behavior of users repository to return null for findByUsername (indicating user doesn't exist)
//        when(users.findByName(USER_NAME)).thenReturn(null);
//
//        // Act
//        userFacade.register(USER_NAME, password);
//
//        // Assert
//        // Optionally, you can add assertions here to verify the behavior of registration,
//        // such as checking if the user has been added to the repository correctly.
//        // For example:
//        verify(users, times(1)).save(any(User.class)); // Assuming you have a method like addUser in your UserRepository that adds a new user
    }

    @Test
    void testRegisterUserAlreadyExists() {
//        // Arrange
//        String existingUsername = "existingUser";
//        String password = "newPassword";
//
//        // Mock behavior of users repository to return a user with the existing USER_NAME
//        when(users.findByName(existingUsername)).thenReturn(new User(existingUsername, "existingPassword"));
//
//        // Act
//        userFacade.register(existingUsername, password);
//
//        // Assert
//        // Optionally, you can add assertions here to verify the behavior of registration,
//        // such as checking if no new user is added to the repository.
//        // For example:
//        verify(users, never()).save(any(User.class)); // Ensure addUser is never called
    }

    @Test
    void testLogin() {
//        // Arrange
//        String USER_NAME = "testUser";
//        String password = "testPassword";
//
//        // Mock behavior of users repository to return a user with the provided USER_NAME
//        when(users.findByName(USER_NAME)).thenReturn(new User(USER_NAME, password)); // Mock findByUsername method
//
//        // Act
//        boolean loginResult = userFacade.login(USER_NAME, password);
//
//        // Assert
//        assertTrue(loginResult); // Check if login is successful
    }

    @Test
    void testLogout() {
//        // Arrange

//        // Act
//        userFacade.logout(USER_NAME);
//
//        // Assert
//        // Optionally, you can add assertions here to verify the behavior of logout,
//        // such as checking if the user is effectively logged out by checking some state in the system.
//        // For example:
//        verify(users, times(1)).logoutUser(USER_NAME); // Assuming you have a method like logoutUser in your UserRepository that marks the user as logged out
    }

    @Test
    void testViewShoppingCart() {
//        userFacade.viewShoppingCart(TOKEN);
//        verify(users).findByToken(TOKEN); // Assuming findByToken method in UserRepository
    }

    @Test
    void testModifyShoppingCart() {
//        userFacade.modifyShoppingCart(TOKEN);
//        verify(carts).findByName(TOKEN); // Assuming findByToken method in CartRepository
    }

    @Test
    void testCheckoutShoppingCart() {
//        userFacade.checkoutShoppingCart(TOKEN);
//        verify(carts).checkoutShoppingCart(TOKEN); // Assuming checkoutShoppingCart method in ShoppingCartRepository
    }

    @Test
    void testCreateGuestSession() {
//        userFacade.createGuestSession();
//        verify(userControllerMock).createGuestSession();
    }

    @Test
    void testTerminateGuestSession() {
//        userFacade.terminateGuestSession();
//        verify(userControllerMock).terminateGuestSession();
    }
}
