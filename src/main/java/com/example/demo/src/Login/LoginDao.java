package com.example.demo.src.Login;

import com.example.demo.src.Login.model.GetLogoutRes;
import com.example.demo.src.Login.model.GetStateLoginRes;
import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;


@Repository
public class LoginDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public void setToken(String email) {
        String setTokenQuery = "update User set token = null where email = ?;";
        Object[] setTokenParams = new Object[]{email};
        this.jdbcTemplate.update(setTokenQuery, setTokenParams);

    }

    public void setAuth(String email) {
        String setAuthQuery = "update User set auth = 0 where email = ?";
        Object[] setAuthParams = new Object[]{email};
        this.jdbcTemplate.update(setAuthQuery, setAuthParams);
    }

    public int checkUserID(String email) {
        String checkUserQuery = "select userId from User where email = ?";
        String checkUserParams = email;
        return this.jdbcTemplate.queryForObject( checkUserQuery, int.class, checkUserParams);
    }

    public String getEmail(int userId) {
        String getEmailQuery = "select email from User where userId =?";
        int getEmailParams = userId;


        return String.valueOf(this.jdbcTemplate.update(getEmailQuery, getEmailParams));

    }



    public GetLogoutRes getAuth(String email) {
        System.out.println("로그아웃할 이메일은" + email);
        String logoutConfirmQuery = "UPDATE User SET status = 0 where email = ?";
        Object[] logoutConfirmParams = new Object[]{
                email};


        this.jdbcTemplate.update(logoutConfirmQuery, logoutConfirmParams);
        return new GetLogoutRes(email, 0);
    }

    public GetStateLoginRes getStateLogin(int userId) {
        // status 1: 로그인 된 상태 -> 로그인 한 사람의 userId 반환
        // status 0: 로그인 안 된 상태 -> userId 0 반환 // default
        GetStateLoginRes getStateLoginRes = new GetStateLoginRes(0, 0);

        String getStateLoginQuery = "select status from User where userId = ?";
        int getStateLoginParams = userId;
        int isLogin= this.jdbcTemplate.queryForObject(getStateLoginQuery,
                int.class
                , getStateLoginParams);
        //세션이 유지된 상태인지 확인하기
        if(isLogin == 1) {

        }

        // 로그인 안 된 상태
        return getStateLoginRes;

    }


}


