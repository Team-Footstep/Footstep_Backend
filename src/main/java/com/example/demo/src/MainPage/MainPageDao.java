package com.example.demo.src.MainPage;

import com.example.demo.src.MainPage.model.GetContentsRes;
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


    /*
     * Trending This Week
     * 누적 stamp 횟수가 높은 순서대로 12개 반환 - stamp가 가장 많이 눌린 "block"
     * - 블럭의 내용을 미리보기
     * */
    public List<GetTrendingFootprintsRes> getTrendingFootsteps(){
        String getTrendingFootprintsQuery = "select b.userId, sap.blockId, b.curPageId, b.content, count(*) as footprintNum\n" +
                "from StampAndPrint sap, Block b\n" +
                "where sap.blockId = b.blockId\n" +
                "    and sap.status=1 and b.status=1\n" +
                "group by blockId\n" +
                "order by count(*) desc\n" +
                "limit 12;";

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

        return this.jdbcTemplate.query(getTrendingFootprintsQuery,
                (rs, rowNum) -> new GetTrendingFootprintsRes(
                        rs.getInt("userId"),
                        rs.getInt("blockId"),
                        rs.getInt("curPageId"),
                        rs.getString("content"),
                        jdbcTemplate.queryForObject(getStampNumQuery,
                                int.class
                                , rs.getInt("blockId")),
                        jdbcTemplate.queryForObject(getFootprintNumQuery,
                                int.class
                                , rs.getInt("blockId"))
                ));
    }


    /*
     * 팔로우한 사람들의 최신 기록 5개
     * user가 팔로우한 사람들의 기록(footprint(P) 기준) 중 최신 5개 기록
     * */
    public List<GetFollowingNewRes> getFollowingNew(int userId){
        List<GetFollowingNewRes> getFollowingNewRes;
        int getFollowingNewParams = userId;

        /* 최신 기록 5개 */
        String getFollowingNewQuery = "select p.userId, p.pageId, p.parentBlockId, p.preview, p.createdAt\n" +
                "from Follow f, Page p\n" +
                "where f.follower = ? and f.status=1\n" +
                "  and f.followee = p.userId and p.status=1 and p.access=1 and p.topOrNot=0\n" +
                "  and p.stampOrPrint = 'P'\n" +
                "order by p.createdAt desc\n" +
                "limit 5;";

        /* content 20 블록 */
        String getContentsQuery = "select b.blockId, b.content\n" +
                "from Block b, Page p\n" +
                "where b.curPageId = p.pageId and p.status=1 and p.access=1\n" +
                "    and p.pageId=? and b.status=1\n" +
                "order by b.orderNum\n" +
                "limit 20;";

        /* stamp, print, comment 개수 */
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

        return this.jdbcTemplate.query(getFollowingNewQuery,
                (rs, rowNum) -> new GetFollowingNewRes(
                        rs.getInt("userId"),
                        rs.getInt("pageId"),
                        rs.getInt("parentBlockId"), // parentBlockId로 stamp, print, comment 수 세기
                        rs.getString("preview"),
                        jdbcTemplate.query(getContentsQuery,  // contents
                                (rk, rowNum_k) -> new GetContentsRes(
                                        rk.getInt("blockId"),
                                        rk.getString("content")
                                ), rs.getInt("pageId")),
                        rs.getString("createdAt").substring(0,10),
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


