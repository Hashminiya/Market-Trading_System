package DomainLayer.Market.User;

public interface IUser {
    public boolean changeState(Istate state);
    public boolean login(String userName, String password);
    public boolean logout();
}
