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
        String createUserQuery = "insert into User (userName, email, job, userImgUrl, introduction) VALUES (?,?, ' ', 'https://placeimg.com/640/480/animals', ' ')";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }

    //중복되는 이메일인지 체크
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    /*
     * 유저 프로필 정보 조회
     * */
    public GetProfileRes getProfile(int userId){
        int getProfileParams = userId;

        String getProfileQuery = "select userId, userImgUrl, userName, job, introduction\n" +
                "from User\n" +
                "where status=1\n" +
                "  and userId=?;";

        String getStampTopPageQuery = "select pageId as topStampPageId, access as topStampPageAccess\n" +
                "from Page\n" +
                "where status=1\n" +
                "  and stampOrPrint = 'S'\n" +
                "  and topOrNot = 1\n" +
                "  and userId=?;";

        String getPrintTopPageQuery = "select pageId as topPrintPageId, access as topPrintPageAccess\n" +
                "from Page\n" +
                "where status=1\n" +
                "  and stampOrPrint = 'P'\n" +
                "  and topOrNot = 1\n" +
                "  and userId=?;";

        // [수정 완료] 미공개 글 - curPageId의 access 가 0이면 미포함
        String getFootprintNumQuery = "select count(distinct stampOrPrintId)\n" +
                "from User u,  StampAndPrint sap, Block b, Page p\n" +
                "where u.userId = sap.followeeId and sap.blockId = b.curPageId and p.pageId = b.curPageId\n" +
                "  and sap.stampOrPrint = 'P' and p.access=1\n" +
                "  and sap.status = 1\n" +
                "  and u.userId=?;";

        String getStampNumQuery = "select count(distinct stampOrPrintId)\n" +
                "from User u,  StampAndPrint sap, Block b, Page p\n" +
                "where u.userId = sap.followeeId and sap.blockId = b.curPageId and p.pageId = b.curPageId\n" +
                "  and sap.stampOrPrint = 'S' and p.access=1\n" +
                "  and sap.status = 1\n" +
                "  and u.userId=?;";


        return this.jdbcTemplate.queryForObject(getProfileQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userId"),
                        jdbcTemplate.queryForObject(getStampTopPageQuery,
                                (rk, rowNum_k) -> new GetStampTopPageRes(
                                        rk.getInt("topStampPageId"),
                                        rk.getInt("topStampPageAccess")
                                ), getProfileParams),
                        jdbcTemplate.queryForObject(getPrintTopPageQuery,
                                (rk, rowNum_k) -> new GetPrintTopPageRes(
                                        rk.getInt("topPrintPageId"),
                                        rk.getInt("topPrintPageAccess")
                                ), getProfileParams),
                        rs.getString("userImgUrl"),
                        rs.getString("userName"),
                        rs.getString("job"),
                        rs.getString("introduction"),
                        jdbcTemplate.queryForObject(getStampNumQuery,
                                int.class
                                , getProfileParams),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , getProfileParams)
                ), getProfileParams);
    }

    /*
     * 사용자가 존재하는지 체크
     * */
    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select userId from User where status=1 and userId = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }

    //유저 정보 변경
    public int modifyUserInfo(BigInteger userId, PatchUserReq patchUserReq) {
        System.out.println(patchUserReq.toString());
        String modifyUserQuery = "update User set job=?, userName=?, userImgUrl=?, introduction=? where userId=?";
        Object[] modifyUserParams = new Object[]{patchUserReq.getJob(), patchUserReq.getUserName(),
                patchUserReq.getUserImgUrl(), patchUserReq.getIntroduction(), userId
        };
        return this.jdbcTemplate.update(modifyUserQuery, modifyUserParams);
    }
    //유저 이메일 정보 변경
    public int modifyEmail(String email, int userId) {
        String modifyEmailQuery = "update User set email = ? where userId=?";
        Object[] modifyEmailParams = new Object[]{email, userId};
        return this.jdbcTemplate.update(modifyEmailQuery, modifyEmailParams);
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

    public int createFootstep(int userId) {
        String createFootstepQuery = "insert into Page (userId, topOrNot, stampOrPrint, depth) value(?, 1, 'P', 0);";
        int createFootstepParams = userId;
        this.jdbcTemplate.update(createFootstepQuery, createFootstepParams);
        return 1;
    }

    public int createFollow(int userId) {
        String createFollowQuery = "insert into Page (userId, topOrNot, stampOrPrint, depth) value(?, 1, 'S', 0);";
        int createFollowParams = userId;
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);
        return 1;
    }

}


