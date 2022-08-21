package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.*;


// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {

    private final UserDao userDao;
    private final EmailCertDao emailCertDao;

    private final UserProvider userProvider;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserService(UserDao userDao, EmailCertDao emailCertDao, UserProvider userProvider, EmailSenderService emailSenderService) {
        this.userDao = userDao;
        this.emailCertDao = emailCertDao;
        this.userProvider = userProvider;
        this.emailSenderService = emailSenderService;
    }

    //회원가입하기
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if (userProvider.checkEmail(postUserReq.getEmail())==1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        // 회원가입해주기
        userDao.createUser(postUserReq);
        return new PostUserRes(postUserReq.getEmail(), postUserReq.getUserName(), 0, null);
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
    //회원가입 토큰체크
    public GetTokenRes signupConfirm(int userId, String email, String token) {
        //1. 토큰 체크
        if (emailCertDao.tokenCheck(email, token) == 1) {
            GetTokenRes getTokenRes = emailCertDao.signupConfirm(userId, email);
            System.out.println("토큰 체크 성공");
            return getTokenRes;
        } else {
            GetTokenRes getTokenRes = new GetTokenRes(userId, email, 0);
            return getTokenRes;
        }

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
        userDao.setToken(email);
    }

    public PatchUserRes modifyUserInfo(BigInteger userId, PatchUserReq patchUserReq) throws BaseException {
        try {
            userDao.modifyUserInfo(userId, patchUserReq);
            System.out.println("modifyMemberInfo result");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new PatchUserRes( patchUserReq.getJob(), patchUserReq.getUserName(), patchUserReq.getIntroduction(), patchUserReq.getUserImgUrl());

    }

    public PatchEmailRes modifyEmail(String email, int userId) throws BaseException {
        try {
            userDao.modifyEmail(email, userId);
            System.out.println("modifyEmail result : " + email);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new PatchEmailRes(userId, email);

    }

    public GetAuthRes modifyConfirm(int userId, GetAuthReq getAuthReq) throws BaseException {
        //1. 인증키 체크
        if (emailCertDao.authCheck(userId, getAuthReq) == 1) {
            GetAuthRes getAuthRes = emailCertDao.modifyConfirm(getAuthReq.getEmail(), userId, getAuthReq.getAuth());
            System.out.println("인증키 체크 성공");
            setAuth(getAuthReq.getEmail());
            return getAuthRes;
        } else {
            throw new BaseException(GET_PATCH_EMAIL);
        }
    }

    public void setAuth(String email) {
        //회원가입 성공시에 토큰 null 로 바꿔주기
        userDao.setAuth(email);
    }






}