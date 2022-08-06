package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigInteger;
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
        if (userProvider.checkEmail(postUserReq.getEmail()) == 1) {
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

    public GetEmailCertRes signupConfirm(GetEmailCertReq getEmailCertReq) {
        //1. 토큰 체크
        if (emailCertDao.tokenCheck(getEmailCertReq)==1) {

            GetEmailCertRes getEmailCertRes = emailCertDao.signupConfirm(getEmailCertReq.getEmail());
            System.out.println("토큰 체크 성공");
            return getEmailCertRes;
        } else {
            GetEmailCertRes getEmailCertRes = new GetEmailCertRes(0);
            return getEmailCertRes;
        }

    }
    public GetUserRes getModifyUserInfo(BigInteger userIdx) {
        return userDao.getModifyUserInfo(userIdx);

    }


    public boolean lastId(UserLoginRes userLoginRes, BigInteger userId) {
        BigInteger max = userDao.lastInsertUser(userLoginRes);
        //max가 userId보다 크면 맞음
        if(max.compareTo(userId)==1){
            return true;
        }


        else return false;
    }

    public void modifyUserInfo(PatchUserReq patchUserReq, BigInteger userId) throws BaseException {
        try {
            int result = userDao.modifyUserInfo(patchUserReq, userId);
            System.out.println("modifyMemberInfo result: " + result);

            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}