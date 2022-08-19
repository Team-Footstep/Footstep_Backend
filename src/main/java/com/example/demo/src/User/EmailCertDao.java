package com.example.demo.src.User;

import com.example.demo.src.User.model.GetAuthReq;
import com.example.demo.src.User.model.GetAuthRes;
import com.example.demo.src.User.model.GetTokenRes;
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
    public int tokenCheck(String email, String token) {
        System.out.println("토큰 체크를 진행하겠습니다.");
        String selectEmailCertQuery = "SELECT EXISTS(SELECT * FROM User WHERE token=? AND email=? AND status=0 AND expiredAt>now())";
        Object[] selectEmailCertParams =  new Object[]{
                token, email
        };
        return this.jdbcTemplate.queryForObject(selectEmailCertQuery, Integer.class, selectEmailCertParams);


    }

    public GetTokenRes signupConfirm(String email) {
        System.out.println("가입완료된 이메일은 " + email);
        String signupConfirmQuery = "UPDATE User SET expired = 0, status = 1 WHERE email=?";
        Object[] signupConfirmParams = new Object[]{
                email};
        this.jdbcTemplate.update(signupConfirmQuery, signupConfirmParams);

        return new GetTokenRes(email, 1);


    }

    public int insertAuth(int authNum, String email) {
        String insertAuthQuery = "UPDATE User SET auth= ? where email = ?";
        Object[] insertAuthParams = new Object[]{authNum, email};
        return this.jdbcTemplate.update(insertAuthQuery, insertAuthParams);

    }
    public int authCheck(int userId, GetAuthReq getAuthReq) {
        System.out.println(getAuthReq.getAuth()+
                getAuthReq.getEmail());
        String authCheckQuery = "SELECT EXISTS(SELECT * FROM User WHERE auth=? AND userId = ? AND expiredAt>now())";
        Object[] authCheckParams =  new Object[]{
                getAuthReq.getAuth(),
                userId
        };
        return this.jdbcTemplate.queryForObject(authCheckQuery, int.class, authCheckParams);


    }


    public GetAuthRes modifyConfirm(String email, int userId, int auth) {
        System.out.println("변경된 이메일은 " + email);
        String modifyConfirmQuery = "UPDATE User SET email = ? WHERE userId = ? AND auth = ?";
        Object[] modifyConfirmParams = new Object[]{email, userId, auth};
        this.jdbcTemplate.update(modifyConfirmQuery, modifyConfirmParams);

        return new GetAuthRes(userId);
    }


    public GetTokenRes loginConfirm(String email) {
        System.out.println("로그인 완료된 이메일은" + email);
        String loginConfirmQuery = "UPDATE User SET expired = 0, status = 1 WHERE email=?";
        Object[] loginConfirmParams = new Object[]{
                email};


        this.jdbcTemplate.update(loginConfirmQuery, loginConfirmParams);
        return new GetTokenRes(email, 1);


    }
}
