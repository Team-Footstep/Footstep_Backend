package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;


// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {

    private final UserDao userDao;
    private final UserProvider userProvider
    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider) {
        this.userDao = userDao;
        this.userProvider = userProvider;
    }
    //회원가입하기
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        // 회원가입해주기
        userDao.createUser(postUserReq);

        // 토큰 만들어주기
        String token = UUID.randomUUID().toString();
        userDao.insertToken(email, token);



    }
}