package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.User.model.GetProfileRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class UserController {
    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;

    public UserController(UserProvider userProvider, UserService userService){
        this.userProvider = userProvider;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/profile/{userId}")
    public BaseResponse<GetProfileRes> getProfile(@PathVariable int userId) {
        try{
            GetProfileRes getProfileRes = userProvider.retrieveProfile(userId);
            return new BaseResponse<>(getProfileRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}