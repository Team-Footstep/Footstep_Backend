package com.example.demo.src.User;

import com.example.demo.src.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;


@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userName, email) VALUES (?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }
    //마지막 유저 인덱스 찾기
    public int lastInsertUser(UserLoginRes userLoginRes){
        String lastInsertQuery = "select Max(userId) from User;";
        Object[] lastInsertParams = new Object[]{userLoginRes.getUserId()};
        return this.jdbcTemplate.update(lastInsertQuery, lastInsertParams);
    }

    //중복되는 이메일인지 체크
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public GetProfileRes getProfile(int userId){
        int getProfileParams = userId;

        String getProfileQuery = "select userId, userImgUrl, userName, job, introduction\n" +
                "from User\n" +
                "where userId=?;";

        String getFootprintNumQuery = "select count(*)\n" +
                "from User u,  StampAndPrint sap\n" +
                "where u.userId = sap.followeeId\n" +
                "    and sap.stampOrPrint = 'P'\n" +
                "    and sap.status = 1\n" +
                "    and u.userId=?;";

        return this.jdbcTemplate.queryForObject(getProfileQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userId"),
                        rs.getString("userImgUrl"),
                        rs.getString("userName"),
                        rs.getString("job"),
                        rs.getString("introduction"),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , getProfileParams)
                ), getProfileParams);
    }

    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select userId from User where userId = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }

    public GetUserRes getModifyUserInfo(BigInteger userIdx){
        BigInteger modifyUserParam = userIdx;
        String modifyUserQuery = "select email, job, userName, userImgUrl, introduction from User where userId=?";

        return this.jdbcTemplate.queryForObject(modifyUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("email"),
                        rs.getString("job"),
                        rs.getString("userName"),
                        rs.getString("userImgUrl"),
                        rs.getString("introduction")),
                        modifyUserParam);
    }

    public int modifyUserInfo(PatchUserReq patchUserReq, BigInteger userId) {
        System.out.println(patchUserReq.toString());
        String modifyUserQuery = "update User set job=?, userName=?, userImgUrl=?, introduction=? where userId=?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getJob(), patchUserReq.getUserName(),
                patchUserReq.getUserImgUrl(), patchUserReq.getIntroduction(), userId
        };

        return this.jdbcTemplate.update(modifyUserQuery, modifyUserParams);
    }
}


