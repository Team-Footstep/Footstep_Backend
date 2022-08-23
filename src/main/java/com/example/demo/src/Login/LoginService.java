package com.example.demo.src.Login;

import com.example.demo.config.BaseException;
import com.example.demo.src.User.EmailCertDao;
import com.example.demo.src.User.EmailSenderService;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.GET_PATCH_EMAIL;
import static com.example.demo.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;


// Service Create, Update, Delete 의 로직 처리
@Service
public class LoginService {

    private final LoginDao loginDao;
    private final EmailCertDao emailCertDao;

    private final LoginProvider loginProvider;
    private final EmailSenderService emailSenderService;

    @Autowired
    public LoginService(LoginDao loginDao, EmailCertDao emailCertDao, LoginProvider loginProvider, EmailSenderService emailSenderService) {
        this.loginDao = loginDao;
        this.emailCertDao = emailCertDao;
        this.loginProvider = loginProvider;
        this.emailSenderService = emailSenderService;
    }

    public String getToken(String email) throws BaseException, MessagingException {
        // 토큰 만들어주기
        String token = UUID.randomUUID().toString();
        emailCertDao.insertToken(token, email);
        emailCertDao.insertExpired(email);
        return token;
    }

    public int getAuth(String email) {
        // 토큰 만들어주기
        Random r = new Random();
        int auth = r.nextInt(888888) + 111111;
        emailCertDao.insertAuth(auth, email);
        emailCertDao.insertExpired(email);
        return auth;
    }

    //로그인 토큰 체크
    public GetTokenRes loginConfirm(int userId, String email, String token) {
        //1. 토큰 체크
        if (emailCertDao.loginTokenCheck(email, token) == 1) {
            GetTokenRes getTokenRes = emailCertDao.loginConfirm(userId, email);
            System.out.println("토큰 체크 성공");
            System.out.println("로그인이 완료되었습니다.");

            return getTokenRes;
        } else {
            GetTokenRes getTokenRes = new GetTokenRes(userId, email, 0);
            return getTokenRes;
        }
    }

    public void setToken(String email) {
        //회원가입 성공시에 토큰 null 로 바꿔주기
        loginDao.setToken(email);
    }

    public void setAuth(String email) {
        //회원가입 성공시에 토큰 null 로 바꿔주기
        loginDao.setAuth(email);
    }






}