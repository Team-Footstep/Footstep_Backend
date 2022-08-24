package com.example.demo.src.Login;

import com.example.demo.config.BaseException;
import com.example.demo.src.Login.model.GetLogoutRes;
import com.example.demo.src.Login.model.GetStateLoginRes;
import com.example.demo.src.User.EmailCertDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LoginProvider {

    private final LoginDao loginDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmailCertDao emailCertDao;

    @Autowired
    public LoginProvider(LoginDao loginDao, EmailCertDao emailCertDao) {
        this.loginDao = loginDao;
        this.emailCertDao = emailCertDao;
    }


    public int checkEmail(String email) throws BaseException {
        try {
            return loginDao.checkEmail(email);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserId(String email) {
        int res = loginDao.checkUserID(email);
        return res;
    }



    public GetLogoutRes getAuth(String email) throws BaseException {
        try {
            if (checkEmail(email) == 1) {
                return loginDao.getAuth(email);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new GetLogoutRes(email, 0);
    }


    public GetStateLoginRes stateLoginConfirm(int userId) throws BaseException{
        try {
            String email = loginDao.getEmail(userId);
            if (checkUserId(email) == 1) {
                return loginDao.getStateLogin(userId);
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return new GetStateLoginRes(userId, 0);

    }
}