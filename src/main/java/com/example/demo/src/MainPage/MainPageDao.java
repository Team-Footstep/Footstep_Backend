package com.example.demo.src.MainPage;

import com.example.demo.src.MainPage.model.GetFollowingNewRes;
import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MainPageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    // Trending this week - 블럭의 내용을 미리보기
    public List<GetTrendingFootprintsRes> getTrendingFootsteps(){
        String getTrendingFootprintsQuery = "select b.userId, sap.blockId, b.content, count(*) as footprintNum\n" +
                "from StampAndPrint sap, Block b\n" +
                "where sap.blockId = b.blockId\n" +
                "    and sap.status=1 and b.status=1\n" +
                "    and sap.stampOrPrint = 'P'\n" +
                "group by blockId\n" +
                "order by count(*) desc\n" +
                "limit 12;";

        String getFootprintNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'P'\n" +
                "  and sap.blockId=?;";

        return this.jdbcTemplate.query(getTrendingFootprintsQuery,
                (rs, rowNum) -> new GetTrendingFootprintsRes(
                        rs.getInt("userId"),
                        rs.getInt("blockId"),
                        rs.getString("content"),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , rs.getInt("blockId"))
                ));
    }

    public List<GetFollowingNewRes> getFollowingNew(int userId){
        List<GetFollowingNewRes> getFollowingNewRes;
        int getFollowingNewParams = userId;

        String getFollowingNewQuery = "select p.userId, p.pageId, b.blockId as parentBlockId, p.preview, p.createdAt\n" +
                "from Follow f, Page p, Block b\n" +
                "where f.follower = ? and f.status=1\n" +
                "  and f.followee = p.userId and p.status=1 and p.access=1 and p.topOrNot=0\n" +
                "  and p.stampOrPrint = 'P'\n" +
                "  and b.childPageId = p.pageId\n" +
                "order by p.createdAt desc\n" +
                "limit 5;";

        String getStampNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'S'\n" +
                "  and sap.blockId=?;";

        String getFootprintNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'P'\n" +
                "  and sap.blockId=?;";

        String getCommentNumQuery = "select count(*)\n" +
                "from Comment c\n" +
                "where c.status=1\n" +
                "  and c.blockId=?;";

        // 세계관 붕괴...
        // 최근 글은 페이지 기준.
        // stamp랑 print는 블럭 기준. => 명확히 할 필요 있음

        return this.jdbcTemplate.query(getFollowingNewQuery,
                (rs, rowNum) -> new GetFollowingNewRes(
                        rs.getInt("userId"),
                        rs.getInt("pageId"),        // pageId로 comment 수 세기
                        rs.getInt("parentBlockId"), // parentBlockId 로 stamp, print 수 세기
                        rs.getString("preview"),
                        rs.getString("createdAt"),
                        jdbcTemplate.queryForObject(getStampNumQuery,
                                int.class
                                , rs.getInt("parentBlockId")),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , rs.getInt("parentBlockId")),
                        jdbcTemplate.queryForObject(getCommentNumQuery,
                                int.class
                                , rs.getInt("parentBlockId"))
                ), getFollowingNewParams);
    }

    // stamp/print 는 자신을 자식 페이지로 가지는 블럭이 stamp/print 된 횟수
    // comment 는 자신의 페이지에 있는 블럭들에 달린 코멘트 개수 (-> 자신을 자식 페이지로 가지는 블럭에 달린 코멘트 수로 바꿔야 하는가?)


    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select userId from User where userId = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
}


