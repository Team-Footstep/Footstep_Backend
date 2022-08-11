package com.example.demo.src.User;

import com.example.demo.src.User.model.GetProfileRes;
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

    /*
     * 유저 프로필 정보 조회
     * */
    public GetProfileRes getProfile(int userId){
        int getProfileParams = userId;

        String getProfileQuery = "select u.userId, p.pageId as topPageId, p.access, u.userImgUrl, u.userName, u.job, u.introduction\n" +
                "from User u, Page p\n" +
                "where u.userId = p.userId and u.status=1 and p.status=1\n" +
                "  and p.topOrNot = 1\n" +
                "  and u.userId=?;";


        // [수정 완료] 미공개 글 - curPageId의 access 가 0이면 미포함
        String getFootprintNumQuery = "select count(distinct stampOrPrintId)\n" +
                "from User u,  StampAndPrint sap, Block b, Page p\n" +
                "where u.userId = sap.followeeId and sap.blockId = b.curPageId and p.pageId = b.curPageId\n" +
                "  and sap.stampOrPrint = 'P' and p.access=1\n" +
                "  and sap.status = 1\n" +
                "  and u.userId=?;";

        return this.jdbcTemplate.queryForObject(getProfileQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userId"),
                        rs.getInt("topPageId"),
                        rs.getInt("access"),
                        rs.getString("userImgUrl"),
                        rs.getString("userName"),
                        rs.getString("job"),
                        rs.getString("introduction"),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , getProfileParams)
                ), getProfileParams);
    }

    /*
     * 사용자가 존재하는지 체크
     * */
    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select userId from User where userId = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
}


