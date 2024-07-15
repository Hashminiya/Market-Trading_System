package UnitTests.DomainLayer.User;

import DomainLayer.Market.User.User;
import DomainLayer.Repositories.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    public void test_saveUser() {
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        Optional<User> savedUser = userRepository.findById("testUser");
        assertTrue(savedUser.isPresent());
        assertEquals("testUser", savedUser.get().getUserName());
    }

    @Test
    public void test_findById_existingUser() {
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUserName());
    }

    @Test
    public void test_findById_nonExistingUser() {
        Optional<User> foundUser = userRepository.findById("nonExistingUser");
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void test_deleteUser() {
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        userRepository.delete(user);
        Optional<User> deletedUser = userRepository.findById("testUser");
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    public void test_findAll() {
        User user1 = new User("user1", "password123", 25, null, false, null);
        User user2 = new User("user2", "password456", 30, null, false, null);
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> allUsers = userRepository.findAll();
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void test_updateUser() {
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        User updatedUser = new User("testUser", "newPassword", 26, null, false, null);
        userRepository.save(updatedUser);

        Optional<User> foundUser = userRepository.findById("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("newPassword", foundUser.get().getPassword());
        assertEquals(26, foundUser.get().getUserAge());
    }

    @Test
    public void test_countUsers() {
        User user1 = new User("user1", "password123", 25, null, false, null);
        User user2 = new User("user2", "password456", 30, null, false, null);
        userRepository.save(user1);
        userRepository.save(user2);

        long count = userRepository.count();
        assertEquals(2, count);
    }

    @Test
    public void test_existsById_existingUser() {
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        assertTrue(userRepository.existsById("testUser"));
    }

    @Test
    public void test_existsById_nonExistingUser() {
        assertFalse(userRepository.existsById("nonExistingUser"));
    }

    @Test
    public void test_deleteAll() {
        User user1 = new User("user1", "password123", 25, null, false, null);
        User user2 = new User("user2", "password456", 30, null, false, null);
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteAll();
        assertEquals(0, userRepository.count());
    }
}
