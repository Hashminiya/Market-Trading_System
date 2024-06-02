package API.controller;
import ServiceLayer.User.UserService;
import API.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserControllerApi {


    private final UserService userService;

    @Autowired
    public UserControllerApi(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/user")
    public UserModel getUser(@RequestParam int id){
        Optional<UserModel> user = userService.getUser(1);
        return user.orElse(null);
    }

}
