package AcceptanceTests;

import ServiceLayer.ServiceFactory;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

public class SetUp {
    private static final String ADMIN_USER_NAME ="SystemManager" ;
    private static final String ADMIN_PASSWORD = "SystemManagerPassword";
    public static String ADMIN_TOKEN;
    public static void setUp(){
        ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();
        if(!serviceFactory.systemAvailable()) {
            ResponseEntity<String> loginResponse = serviceFactory.getUserService().login(ADMIN_USER_NAME, ADMIN_PASSWORD);
            ADMIN_TOKEN = loginResponse.getBody();
            serviceFactory.initFactory(ADMIN_TOKEN);
        }
    }
}
