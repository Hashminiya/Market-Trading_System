package DomainLayer.Market.User;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;

public class PasswordUtil {
    public static String hashPassword(String plainTextPassword, String salt) {
        return new Sha256Hash(plainTextPassword, salt, 500000).toHex();
    }

    public static String generateSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }
}

