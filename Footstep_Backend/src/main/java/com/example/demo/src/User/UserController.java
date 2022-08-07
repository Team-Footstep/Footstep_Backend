package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

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
    Map<String, String> map = new HashMap<String, String>();
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
            postUserRes.setToken(getToken);
            //이메일 보내주기
            System.out.println("이메일 보내겠습니다.~");
            emailSenderService.sendSignupMail(getToken, postUserRes.getEmail());
            //map에 저장해주기

            map.put("email", postUserRes.getEmail());
            map.put("token", getToken);
            System.out.println(map);
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
    public BaseResponse <GetTokenRes> signupConfirm(@RequestBody GetTokenReq getTokenReq){
        System.out.println("클릭한 이메일은 : " + getTokenReq.getEmail());
        String ctoken = (String) map.get("token");
        getTokenReq.setToken(ctoken);
        GetTokenRes getTokenRes =
                userService.signupConfirm(getTokenReq);
        System.out.println("회원가입이 완료되었습니다.");

        //회원 가입 완료 후 -> 토큰 값 null로 바꿔주기
        userService.setToken(getTokenReq.getEmail());
        return new BaseResponse<>(getTokenRes);

    }
    /**
     * 로그인 API
     * [GET] /users/login
     *
     * @return BaseResponse<PostUserRes>
     */

    /**
     * 유저정보변경 이메일 인증번호 API
     * [GET] /users/modify/confirm
     *
     * @return BaseResponse<PostUserRes>

    */
    @ResponseBody
    @GetMapping("/users/modify/confirm") // (GET) 127.0.0.1:8080/users/modify/confirm
    public String authConfirm(@RequestBody GetAuthReq getAuthReq) throws MessagingException {
        System.out.println("수정을 원하는 이메일은 : " + getAuthReq.getEmail());
        System.out.println("이메일 인증 요청이 들어옴!");
        return emailSenderService.sendAuthEmail(getAuthReq.getEmail());

    }

    /**
     * 유저정보변경 API
     * [PATCH] /modifyUser
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/modify/{userId}") // (PATCH) 127.0.0.1:8080/users/modify/{userId}
    public BaseResponse<String> modifyUserInfo(@PathVariable("userId") BigInteger userId, @RequestBody PatchUserReq patchUserReq) throws BaseException, MessagingException {
        //userId 가 없을때
        if(userId == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        System.out.println("정보수정하는데까지 옴");
        userService.modifyUserInfo(patchUserReq, userId);
        emailSenderService.sendAuthEmail(patchUserReq.getEmail());
        String result = patchUserReq.getUserName() + " 정보 수정 완료";
        return new BaseResponse<>(result);
    }



    /**
     * 유저 프로필 정보 조회 API
     * [GET] /users/profile/{userId}
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