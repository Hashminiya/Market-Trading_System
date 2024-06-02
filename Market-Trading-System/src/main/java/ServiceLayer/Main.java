package ServiceLayer;
import javax.ws.rs.core.Response;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a Admin user name: ");
        String userName = scanner.nextLine();
        System.out.print("Enter a Admin password: ");
        String password = scanner.nextLine();

        ServiceFactory serviceFactory = ServiceFactory.getServiceFactory();
        Response loginResponse = serviceFactory.getUserService().login(userName, password);
        serviceFactory.initFactory((String) loginResponse.getEntity());
    }
}
