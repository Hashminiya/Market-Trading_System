package ServiceLayer.User;

import javax.ws.rs.core.Response;

public interface IUserService{
    public Response GuestEntry();
    public Response GuestExit(int GuestID);
    public Response register(String userName, String password);
    public Response login(String userName, String password);
    public Response logout(String userName);
    public Response viewShoppingCart(String token);
    public Response modifyShoppingCart(String token);
    public Response checkoutShoppingCart(String token);
}
