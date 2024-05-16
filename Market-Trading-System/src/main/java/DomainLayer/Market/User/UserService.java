package DomainLayer.Market.User;

import DomainLayer.Market.IRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

import static DomainLayer.Market.User.PasswordUtil.generateSalt;
import static DomainLayer.Market.User.PasswordUtil.hashPassword;

public class UserService {
    private final IRepository<String,User> userRepository; // Assume you have a UserRepository for database operations

    public UserService(IRepository<String,User> userRepository) {
        this.userRepository = userRepository;
    }

    // Method to create a new user with hashed password and salt
    public void createUser(String username, String plainTextPassword) {
        String salt = generateSalt();
        String hashedPassword = hashPassword(plainTextPassword, salt);

        User user = new User(username, hashedPassword, salt);
        userRepository.save(user);
    }


    public void login(String username, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            currentUser.login(token);
        } catch (AuthenticationException ae) {
            // Handle failed login
            throw new RuntimeException("Login failed", ae);
        }
    }

    public boolean isPermitted(String permission) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted(permission);
    }

    public boolean hasRole(String role) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.hasRole(role);
    }

    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    // Method to verify the user's password
    public boolean verifyPassword(String username, String plainTextPassword) {
        User user = userRepository.findById(username);

        if (user == null) {
            return false; // User not found
        }

        String storedHashedPassword = user.getHashedPassword();
        String storedSalt = user.getSalt();

        String computedHash = hashPassword(plainTextPassword, storedSalt);

        return storedHashedPassword.equals(computedHash);
    }

    // Additional methods for user management (e.g., find user, update user, etc.) can be added here
}

