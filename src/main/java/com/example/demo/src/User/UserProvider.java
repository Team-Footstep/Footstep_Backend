package com.example.demo.src.User;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.User.model.GetProfileRes;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserProvider {

    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmailCertDao emailCertDao;

    @Autowired
    public UserProvider(UserDao userDao, EmailCertDao emailCertDao) {
        this.userDao = userDao;
        this.emailCertDao = emailCertDao;
    }

    /*
     * 유저 프로필 정보 조회
     * */
    public GetProfileRes retrieveProfile(int userId) throws BaseException {
        if(checkUserExist(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            return userDao.getProfile(userId);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 사용자가 존재하는지 체크
     * */
    public int checkUserExist(int userId) throws BaseException{
        try{
            return userDao.checkUserExist(userId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(String email) {
        int res = userDao.checkUserID(email);
        return res;
    }

}