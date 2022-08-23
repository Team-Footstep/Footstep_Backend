package com.example.demo.src.Login;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Login.model.GetLoginReq;
import com.example.demo.src.Login.model.GetLoginRes;
import com.example.demo.src.Login.model.GetLogoutRes;
import com.example.demo.src.Login.model.GetStateLoginRes;
import com.example.demo.src.User.SessionConst;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class LoginController {
    @Autowired
    private final LoginProvider loginProvider;
    @Autowired
    private final LoginService loginService;
    private final LoginEmailSenderService loginEmailSenderService;
    Map<String, String> loginmap = new HashMap<String, String>();

    HttpSession session;
    public LoginController(LoginProvider loginProvider, LoginService loginService,LoginEmailSenderService loginEmailSenderService){
        this.loginProvider = loginProvider;
        this.loginService = loginService;
        this.loginEmailSenderService = loginEmailSenderService;
    }

    /**
     * 로그인 API
     * [POST] /users/login
     *
     */
    @ResponseBody
    @PostMapping("/login") // (POST) 127.0.0.1:8080/users/login
    public BaseResponse<GetLoginRes> login(@RequestBody GetLoginReq getLoginReq) throws BaseException, MessagingException {

        loginProvider.checkEmail(getLoginReq.getEmail());
        System.out.println("이메일 체크 완료");

        //토큰값 생성해서 메일 보내주기
        String ctoken = loginService.getToken(getLoginReq.getEmail());
        getLoginReq.setToken(ctoken);
        System.out.println("로그인 토큰은 " + ctoken);
        GetLoginRes getLoginRes = loginEmailSenderService.loginMail(getLoginReq);

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
    public BaseResponse <GetTokenRes> confirmlogin(HttpServletRequest request, @RequestParam("email")String email, @RequestParam("token")String token) throws BaseException, MessagingException {
        System.out.println("클릭한 이메일은 : " + email);
        int userId = loginProvider.checkUserId(email);
        System.out.println("해당 이메일의 userId 값은 " + userId);
        GetTokenRes getTokenRes = loginService.loginConfirm(userId, email, token);

        //인증이 완료되었으므로, 세션 생성하기
        session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userId);

        System.out.println("세션값은 " + session);


        //로그인 완료후 해당 유저의 내 풋스텝/팔로우 가지고 오기
        loginProvider.getFootstep(userId);
        loginProvider.getFollow(userId);

        //로그인 완료 후 -> 토큰 값 null로 바꿔주기
        loginService.setToken(email);
        //TODO : 페이지 전환
        return new BaseResponse<>(getTokenRes);
    }

    /**
     * 로그 아웃 API
     * [POST] /users/logout
     *
     */
    @PostMapping("/logout") // (POST) 127.0.0.1:8080/users/login
    public BaseResponse<com.example.demo.src.Login.model.GetLogoutRes> logout(HttpServletRequest request, @RequestParam("email")String email) throws BaseException {
        GetLogoutRes getLogoutRes = loginProvider.getAuth(email);

        session = request.getSession(false);
        if(session.getAttribute(SessionConst.LOGIN_MEMBER)!=null) {
            session.removeAttribute(SessionConst.LOGIN_MEMBER);
            session.invalidate();
            System.out.println("로그아웃 완료");
            System.out.println(session);

        }

        return new BaseResponse<>(getLogoutRes);
    }
    //로그인된 상태인지 아닌지 확인
//    /**
//     * 로그인 여부 확인 API
//     * [GET] /users/stateLogin
//     *
//     */
//    @ResponseBody
//    @GetMapping("/stateLogin") // (POST) 127.0.0.1:8080/users/stateLogin
//    public BaseResponse <GetStateLoginRes> statelogin(HttpServletRequest request, @RequestParam("userId")int userId) throws BaseException, MessagingException {
//        System.out.println("로그인 여부를 확인할 userId는 : " + userId);
//        GetStateLoginRes getStateLoginRes = loginProvider.stateLoginConfirm(userId);
//
//
//        return new BaseResponse<>(getStateLoginRes);
//    }

}