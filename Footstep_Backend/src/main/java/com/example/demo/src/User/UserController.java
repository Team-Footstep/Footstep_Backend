package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.User.model.*;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class UserController {
    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    public UserController(UserProvider userProvider, UserService userService, EmailSenderService emailSenderService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * 회원가입 API
     * [POST] /users/signup
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/signup") // (POST) 127.0.0.1:8080/users/signup
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        //이메일이 입력되지 않았을때
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이름이 입력되지 않았을떄
        if(postUserReq.getUserName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_USERNAME);
        }
        // 이메일 정규표현이 맞지 않을때
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            String getToken = userService.getToken(postUserRes.getEmail());
            System.out.println("토큰은 " + getToken);
            //이메일 보내주기
            System.out.println("이메일 보내겠습니다.~");
            emailSenderService.sendMail(getToken, postUserRes.getEmail());

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * 회원가입 이메일 API
     * [GET] /users/signup/confirm
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @GetMapping("/signup/confirm") // (POST) 127.0.0.1:8080/users/signup/confirm
    public BaseResponse <GetEmailCertRes> signupConfirm(@RequestBody GetEmailCertReq getEmailCertReq){
        System.out.println("클릭한 이메일은 : " + getEmailCertReq.getEmail());

        GetEmailCertRes getEmailCertRes =
                userService.signupConfirm(getEmailCertReq);

        return new BaseResponse<>(getEmailCertRes);

    }

    /**
     * 유저 프로필 정보 조회 API
     * [POST] /users/profile/{userId}
     * 유저아이디, 프로필 사진 url, 이름, 직업, 자기소개, 총 footprint 받은 횟수
     * */
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