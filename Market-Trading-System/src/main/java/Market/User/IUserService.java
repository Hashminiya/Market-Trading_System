package Market.User;

public interface IUserService{
    public void GuestEntry();
    public void GuestExit(int GuestID);
    public void register(String userName, String password);
    public void login(String userName, String password);
    public void logout(String userName);
    public String viewShoppingCart(String token);
    public void modifyShoppingCart(String token);
    public void checkoutShoppingCart(String token);
}
