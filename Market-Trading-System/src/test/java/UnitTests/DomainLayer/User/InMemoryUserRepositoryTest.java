package UnitTests.DomainLayer.User;

import API.Utils.SpringContext;
import DomainLayer.Market.User.User;
import DomainLayer.Repositories.InMemoryUserRepository;
import DomainLayer.Repositories.UserRepository;
import SetUp.ApplicationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InMemoryUserRepositoryTest {

    private static InMemoryUserRepository userRepository;
    private static boolean inMemory = true;

    @BeforeAll
    public static void init() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("src/test/resources/application.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String activeProfile = props.getProperty("spring.profiles.active", "default");
        if ("db".equals(activeProfile)) {
            inMemory = false;
        }
        if(inMemory)
            userRepository = SpringContext.getBean(InMemoryUserRepository.class);
    }

    @BeforeEach
    public void setUp() {
        if(inMemory)
            userRepository.deleteAll();
    }

    @Test
    public void test_saveUser() {
        if (!inMemory) return;
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        Optional<User> savedUser = userRepository.findById("testUser");
        assertTrue(savedUser.isPresent());
        assertEquals("testUser", savedUser.get().getUserName());
    }

    @Test
    public void test_findById_existingUser() {
        if (!inMemory) return;
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById("testUser");
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUserName());
    }

    @Test
    public void test_findById_nonExistingUser() {
        if (!inMemory) return;
        Optional<User> foundUser = userRepository.findById("nonExistingUser");
        assertTrue(foundUser.isEmpty());
    }

    @Test
    public void test_deleteUser() {
        if (!inMemory) return;
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        userRepository.delete(user);
        Optional<User> deletedUser = userRepository.findById("testUser");
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    public void test_findAll() {
        if (!inMemory) return;
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
        if (!inMemory) return;
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
        if (!inMemory) return;
        User user1 = new User("user1", "password123", 25, null, false, null);
        User user2 = new User("user2", "password456", 30, null, false, null);
        userRepository.save(user1);
        userRepository.save(user2);

        long count = userRepository.count();
        assertEquals(2, count);
    }

    @Test
    public void test_existsById_existingUser() {
        if (!inMemory) return;
        User user = new User("testUser", "password123", 25, null, false, null);
        userRepository.save(user);

        assertTrue(userRepository.existsById("testUser"));
    }

    @Test
    public void test_existsById_nonExistingUser() {
        if (!inMemory) return;
        assertFalse(userRepository.existsById("nonExistingUser"));
    }

    @Test
    public void test_deleteAll() {
        if (!inMemory) return;
        User user1 = new User("user1", "password123", 25, null, false, null);
        User user2 = new User("user2", "password456", 30, null, false, null);
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteAll();
        assertEquals(0, userRepository.count());
    }
}
