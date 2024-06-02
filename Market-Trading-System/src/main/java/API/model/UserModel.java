package API.model;

public class UserModel {

    String userName;
    String password;
    int id;

    public UserModel(String userName, String password,int id){
        this.userName = userName;
        this.password = password;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
