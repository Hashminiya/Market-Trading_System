package DomainLayer.Market.User;

public interface IUser {
    public boolean login(String userName, String password);
    public boolean logout();
}
