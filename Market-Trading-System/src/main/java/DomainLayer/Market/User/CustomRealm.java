package DomainLayer.Market.User;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class CustomRealm extends AuthorizingRealm {

    private UserService userService; // Dependency on UserService

    // Constructor
    public CustomRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        User user = getUserFromDatabase(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(user.getRoles());
        authorizationInfo.setStringPermissions(user.getPermissions());

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        User user = getUserFromDatabase(username);
        if (user == null) {
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }

        return new SimpleAuthenticationInfo(username, user.getHashedPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }

    private User getUserFromDatabase(String username) {
        // Retrieve user from the database
        // Example: return userRepository.findByUsername(username);
        return null; // Placeholder
    }
}
