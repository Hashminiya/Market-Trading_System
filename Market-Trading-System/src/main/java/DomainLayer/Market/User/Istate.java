package DomainLayer.Market.User;

public interface Istate {
    public boolean checkPermission(Permission permission);
    public List<Permission> getPermission();
}
