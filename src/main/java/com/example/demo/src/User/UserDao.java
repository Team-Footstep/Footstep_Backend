package com.example.demo.src.User;

import com.example.demo.src.User.model.GetProfileRes;
import com.example.demo.src.User.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


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

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    //중복되는 이메일인지 체크
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }
    //해당되는 이메일에 토큰 업데이트 해주기
    public int insertToken(String email, String token){
        String insertToken = ""
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
}


