package com.example.demo.src.User;

import com.example.demo.src.User.model.GetEmailCertReq;
import com.example.demo.src.User.model.GetEmailCertRes;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository

public class EmailCertDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    //해당되는 이메일에 토큰 업데이트 해주기
    public int insertToken(String token, String email){
        String insertTokenQuery = "UPDATE User SET token = ? where email = ?";
        Object[] insertTokenParams = new Object[]{token, email};
        return this.jdbcTemplate.update(insertTokenQuery, insertTokenParams);
    }
    //만료 시간 지정해주기
    public int insertExpired(String email){
        String insertExpiredQuery = "UPDATE User SET expiredAt = (DATE_ADD(NOW(), INTERVAL 5 MINUTE))  where email = ?;";
        Object[] insertExpiredParams = new Object[]{email};
        return this.jdbcTemplate.update(insertExpiredQuery, insertExpiredParams);
    }

    //토큰 체크
    public Integer tokenCheck(GetEmailCertReq getEmailCertReq) {
        System.out.println(getEmailCertReq.getToken()+
                getEmailCertReq.getEmail());
        String selectEmailCertQuery = "SELECT EXISTS(SELECT * FROM User WHERE token=? AND email=? AND status=0 AND expiredAt>now())";
        Object[] selectEmailCertParams =  new Object[]{
                getEmailCertReq.getToken(),
                getEmailCertReq.getEmail()
        };
        System.out.println(this.jdbcTemplate.queryForObject(selectEmailCertQuery, Integer.class, selectEmailCertParams));
        return this.jdbcTemplate.queryForObject(selectEmailCertQuery, Integer.class, selectEmailCertParams);


    }

    public GetEmailCertRes signupConfirm(String email) {
        System.out.println("가입완료된 이메일은 " + email);
        String signupConfirmQuery = "UPDATE User SET expired = 0, status = 1 WHERE email=?";
        Object[] signupConfirmParams = new Object[]{
                email};


        return new GetEmailCertRes(this.jdbcTemplate.update(signupConfirmQuery, signupConfirmParams));


    }
}
