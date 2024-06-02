//package api;
//
//import api.model.UserModel;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserServiceAPI {
//
//    private final List<UserModel> users;
//
//    public UserServiceAPI(){
//        users = new ArrayList<>();
//        users.add(new UserModel("noam", "123", 1));
//    }
//
//    public Optional<UserModel> getUser(int id){
//        Optional<UserModel> optional = Optional.empty();
//        optional = Optional.of(users.get(0));
//        return optional;
//    }
//}
