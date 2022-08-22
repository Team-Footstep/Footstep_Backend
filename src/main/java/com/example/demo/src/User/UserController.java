package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Member;
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
    Map<String, String> loginmap = new HashMap<String, String>();

    Map<String, Integer> emap = new HashMap<String, Integer>();
    HttpSession session;
    String sessionId;
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
        if(postUserReq.getEmail() == "" ){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이름이 입력되지 않았을떄
        if(postUserReq.getUserName() == ""){
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
     * 회원가입 이메일 검증API
     * [GET] /users/signup/confirm
     *
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @GetMapping("/signup/confirm") // (POST) 127.0.0.1:8080/users/signup/confirm
    public BaseResponse <GetTokenRes> signupConfirm(@RequestParam("email")String email, @RequestParam("token")String token){
        System.out.println("클릭한 이메일은 : " + email);
        int userId = userProvider.checkUserId(email);
        GetTokenRes getTokenRes = userService.signupConfirm(userId, email, token);
        System.out.println("회원가입이 완료되었습니다.");

        //회원 가입 완료 후 -> 토큰 값 null로 바꿔주기
        userService.setToken(email);
        return new BaseResponse<>(getTokenRes);

    }
    /**
     * 로그인 API
     * [POST] /users/login
     *
     */
    @ResponseBody
    @PostMapping("/login") // (POST) 127.0.0.1:8080/users/login
    public BaseResponse<GetLoginRes> login(@RequestBody GetLoginReq getLoginReq) throws BaseException, MessagingException {

        userProvider.checkEmail(getLoginReq.getEmail());
        System.out.println("이메일 체크 완료");

        //토큰값 생성해서 메일 보내주기
        String ctoken = userService.getToken(getLoginReq.getEmail());
        getLoginReq.setToken(ctoken);
        System.out.println("로그인 토큰은 " + ctoken);
        GetLoginRes getLoginRes =  emailSenderService.loginMail(getLoginReq);

        //map에 저장해주기
        loginmap.put("email", getLoginReq.getEmail());
        loginmap.put("token", getLoginReq.getToken());
        return new BaseResponse<>(getLoginRes);
    }
    /**
     * 로그인 검증 API
     * [GET] /users/confirmlogin
     *
     */
    @ResponseBody
    @GetMapping("/confirmlogin") // (POST) 127.0.0.1:8080/users/confirmlogin
    public BaseResponse <GetTokenRes>  confirmlogin(HttpServletRequest request, @RequestParam("email")String email, @RequestParam("token")String token) throws BaseException, MessagingException {
        System.out.println("클릭한 이메일은 : " + email);
        int userId = userProvider.checkUserId(email);
        System.out.println("해당 이메일의 userId 값은 " + userId);
        GetTokenRes getTokenRes = userService.loginConfirm(userId, email, token);

        //인증이 완료되었으므로, 세션 생성하기
        session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userId);

        System.out.println("세션값은 " + session);

        //로그인 완료후 해당 유저의 내 풋스텝/팔로우 가지고 오기
        userProvider.getFootstep(userId);
        userProvider.getFollow(userId);

        //로그인 완료 후 -> 토큰 값 null로 바꿔주기
        userService.setToken(email);
        //TODO : 페이지 전환
        return new BaseResponse<>(getTokenRes);
    }

    /**
     * 로그 아웃 API
     * [POST] /users/logout
     *
     */
    @PostMapping("/logout") // (POST) 127.0.0.1:8080/users/login
    public BaseResponse<GetLogoutRes> logout(HttpServletRequest request, @RequestParam("email")String email) throws BaseException {
        GetLogoutRes getLogoutRes = userProvider.getAuth(email);

        session = request.getSession(false);
        if(session.getAttribute(SessionConst.LOGIN_MEMBER)!=null) {
            session.removeAttribute(SessionConst.LOGIN_MEMBER);
            session.invalidate();
            System.out.println("로그아웃 완료");
            System.out.println(session);

        }

        return new BaseResponse<>(getLogoutRes);
    }

    /**
     * 유저정보변경 (이메일 제외) API
     * [PATCH] /modify/{userId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/modify/{userId}") // (PATCH) 127.0.0.1:8080/users/modify/{userId}
    public BaseResponse<PatchUserRes> modifyUserInfo(HttpServletRequest request, @PathVariable("userId") BigInteger userId, @RequestBody PatchUserReq patchUserReq) throws BaseException, MessagingException {
        //userId 가 없을때
        if(userId == null) {
            return new BaseResponse<>(EMPTY_IDX);
        }

        //로그인 여부 확인하기
        if(session == null || !request.isRequestedSessionIdValid()){
            System.out.println("로그인 상태가 아닙니다.");
            return new BaseResponse<>(NOT_LOGIN);
        }
        else{
            System.out.println("--정보수정--");
            PatchUserRes patchUserRes = userService.modifyUserInfo(userId, patchUserReq);
            String result = patchUserReq.getUserName() + " 정보 수정 완료";
            System.out.println(result);
            return new BaseResponse<>(patchUserRes);


        }

    }
    /**
     * 이메일 변경 API
     * [PATCH] /modifyEmail/{userId}
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/modifyEmail/{userId}") // (PATCH) 127.0.0.1:8080/users/modifyEmail/{userId}
    public BaseResponse<PatchEmailRes> modifyEmail(HttpServletRequest request, @PathVariable("userId") int userId,  @RequestBody PatchEmailReq patchEmailReq) throws BaseException, MessagingException {
        //userId 가 없을때
        if(userId == 0) {
            return new BaseResponse<>(EMPTY_IDX);
        }
        //이메일 정규식인지 확인
        if(!isRegexEmail(patchEmailReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //이미 DB에 존재하는 이메일일때
        if(userProvider.checkEmail(patchEmailReq.getEmail())==1){
            return new BaseResponse<>(POST_USERS_EXISTS_EMAIL);
        }
        if(session == null || !request.isRequestedSessionIdValid()){
            System.out.println("로그인 상태가 아닙니다.");
            return new BaseResponse<>(NOT_LOGIN);
        }
        else {
            PatchEmailRes patchEmailRes = userService.modifyEmail(patchEmailReq.getEmail(), userId);

            int getAuth = userService.getAuth(patchEmailReq.getEmail());
            System.out.println("인증번호 값은 " + getAuth);
            patchEmailReq.setAuth(getAuth);
            //map에 저장해주기
            emap.put("userId", userId);
            emap.put("auth", getAuth);
            System.out.println(map);

            //이메일 보내주기
            System.out.println("이메일 보내겠습니다.~");
            emailSenderService.sendAuthEmail(getAuth, patchEmailReq.getEmail());

            return new BaseResponse<>(patchEmailRes);
        }
    }

    /**
     * API
     * [GET] /users/modifyEmail/confirm/userId
     * 이메일 수정 후 이메일 인증 api
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @GetMapping("/modifyEmail/confirm/{userId}") // (POST) 127.0.0.1:8080/users/modify/confirm/userId
    public BaseResponse <GetAuthRes> modifyConfirm(@PathVariable int userId, @RequestBody GetAuthReq getAuthReq) throws BaseException {

        int auth = emap.get("auth");
        getAuthReq.setAuth(auth);
        System.out.println("auth는 " + getAuthReq.getAuth());
        //입력된 auth값과 db의 auth값이 같으면 업데이트
        GetAuthRes getAuthRes = userService.modifyConfirm(userId, getAuthReq);

        return new BaseResponse<>(getAuthRes);

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