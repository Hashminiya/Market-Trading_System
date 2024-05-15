package UnitTests;

import DomainLayer.Market.IRepository;
import DomainLayer.Market.User.IUserFacade;
import DomainLayer.Market.User.ShoppingCart;
import DomainLayer.Market.User.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserFacadeUT {

    @Mock
    private IRepository<ShoppingCart> carts;

    @Mock
    private IRepository<User> users;

    @InjectMocks
    private IUserFacade userFacade = new UserController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes annotated fields
    }

    @Test
    void testRegister() {
        // Arrange
        String username = "newUser";
        String password = "newPassword";

        // Mock behavior of users repository to return null for findByUsername (indicating user doesn't exist)
        when(users.findByName(username)).thenReturn(null);

        // Act
        userFacade.register(username, password);

        // Assert
        // Optionally, you can add assertions here to verify the behavior of registration,
        // such as checking if the user has been added to the repository correctly.
        // For example:
        verify(users, times(1)).save(any(User.class)); // Assuming you have a method like addUser in your UserRepository that adds a new user
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Arrange
        String existingUsername = "existingUser";
        String password = "newPassword";

        // Mock behavior of users repository to return a user with the existing username
        when(users.findByName(existingUsername)).thenReturn(new User(existingUsername, "existingPassword"));

        // Act
        userFacade.register(existingUsername, password);

        // Assert
        // Optionally, you can add assertions here to verify the behavior of registration,
        // such as checking if no new user is added to the repository.
        // For example:
        verify(users, never()).save(any(User.class)); // Ensure addUser is never called
    }

    @Test
    void testLogin() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";

        // Mock behavior of users repository to return a user with the provided username
        when(users.findByName(username)).thenReturn(new User(username, password)); // Mock findByUsername method

        // Act
        boolean loginResult = userFacade.login(username, password);

        // Assert
        assertTrue(loginResult); // Check if login is successful
    }

    @Test
    void testLogout() {
        // Arrange
        String username = "testUser";

        // Act
        userFacade.logout(username);

        // Assert
        // Optionally, you can add assertions here to verify the behavior of logout,
        // such as checking if the user is effectively logged out by checking some state in the system.
        // For example:
        verify(users, times(1)).logoutUser(username); // Assuming you have a method like logoutUser in your UserRepository that marks the user as logged out
    }

    @Test
    void testViewShoppingCart() {
        String token = "testToken";

        userFacade.viewShoppingCart(token);
        verify(users).findByToken(token); // Assuming findByToken method in UserRepository
    }

    @Test
    void testModifyShoppingCart() {
        String token = "testToken";

        userFacade.modifyShoppingCart(token);
        verify(carts).findByName(token); // Assuming findByToken method in CartRepository
    }

    @Test
    void testCheckoutShoppingCart() {
        String token = "testToken";

        userFacade.checkoutShoppingCart(token);
        verify(carts).checkoutShoppingCart(token); // Assuming checkoutShoppingCart method in ShoppingCartRepository
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
}
