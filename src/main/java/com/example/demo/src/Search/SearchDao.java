package com.example.demo.src.Search;

import com.example.demo.src.Search.model.GetContentsRes;
import com.example.demo.src.Search.model.GetUserInfoRes;
import com.example.demo.src.Search.model.GetPostsInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * @apiNote [GET] /search/word
     * @param word : 검색어
     * @return 유저 정보
     * @author 문현우
     */
    public List<GetUserInfoRes> getUserInfoByWord(String word){
        String getUserInfoByWordQuery="SELECT User.userId,userName,introduction,job,userImgUrl,Page.pageId,Page.topOrNot\n" +
                "FROM User,Page\n" +
                "WHERE userName LIKE concat('%',?,'%') AND User.status =1 and Page.userId = User.userId\n" +
                "AND Page.topOrNot = 1";
        List<GetUserInfoRes> userInfoList = this.jdbcTemplate.query(getUserInfoByWordQuery,
                (rs,rowNum) -> new GetUserInfoRes(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("introduction"),
                        rs.getString("job"),
                        rs.getString("userImgUrl"),
                        rs.getInt("pageId")
                ),word);
        return userInfoList;
    }

    public List<GetPostsInfoRes> getPostInfo(String word) {

        //검색어로 글 정보 조회하는 쿼리
        String getPostInfoByWordQuery="SELECT User.userId,Page.preview,Page.updatedAt,Page.parentBlockId,Page.pageId\n" +
                "FROM User,Page\n" +
                "WHERE preview L" +
                "IKE concat('%',?,'%') and Page.userId = User.userId\n" +
                "and User.status =1 and Page.access = 1";

        // 유저 Id로 유저 정보 조회하는 쿼리
        String getUserInfoByIdQuery ="SELECT User.userId,userName,introduction,job,userImgUrl,Page.pageId\n" +
                "FROM User,Page\n" +
                "WHERE User.userId = ? and User.userId = Page.userId and Page.topOrNot = 1;";

        String getContentListByPageId ="SELECT bl.blockId,bl.content,pg.pageId,bl.content,bl.orderNum\n" +
                "FROM Block bl, Page pg\n" +
                "WHERE pg.pageId = bl.curPageId and pg.pageId = ?\n" +
                "order by bl.orderNum";
        // 하위 페이지가 되는 블록의Id를 받아와서 Stamp 개수
        String getStampNumByBlockIdQuery="select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1 and sap.stampOrPrint = 'S' and sap.blockId=?";
        // 하위 페이지가 되는 블록의Id를 받아와서 footPrint 개수
        String getFootPrintNumByBLockIdQuery= "select count(*)\n" +
                "        from StampAndPrint sap\n" +
                "        where sap.status=1 and sap.stampOrPrint = 'P' and sap.blockId=?";
        // 하위 페이지가 되는 블록의Id를 받아와서 Comment 개수
        String getCommentNumByBLockIdQuery = "SELECT COUNT(*)\n" +
                "FROM Comment COM\n" +
                "WHERE COM.status=1 AND COM.blockId = ?";
        //proceed selectUserInfoByIdQuery2 는 필요없는 쿼리
        List<GetPostsInfoRes> postInfoList = this.jdbcTemplate.query(getPostInfoByWordQuery,
                (rs,rowNum)-> new GetPostsInfoRes(

                        //검색한 글의 -> 유저 정보
                        this.jdbcTemplate.queryForObject(getUserInfoByIdQuery,
                                (rs1,rowNum3) -> new GetUserInfoRes(
                                        rs1.getInt("userId"),
                                        rs1.getString("userName"),
                                        rs1.getString("introduction"),
                                        rs1.getString("job"),
                                        rs1.getString("userImgUrl"),
                                        rs1.getInt("pageId")
                                ),rs.getInt("userId")
                        ),
                        // 검색한 글의 -> 프리뷰, 부모 블럭Id, 업데이트 시각
                        rs.getString("preview"),
                        rs.getInt("parentBlockId"),
                        rs.getTimestamp("updatedAt"),

                        //검색한 글의 컨텐츠
                        this.jdbcTemplate.query(getContentListByPageId,
                                (rs2,rowNum4) -> new GetContentsRes(
                                        rs2.getInt("blockId"),
                                        rs2.getString("content")
                                ),rs.getString("pageId")
                        ),

                        //blockId -> commentNum
                        this.jdbcTemplate.queryForObject(getCommentNumByBLockIdQuery,
                                int.class,rs.getInt("parentBlockId")),
                        //blockId -> StampNum
                        this.jdbcTemplate.queryForObject(getStampNumByBlockIdQuery,
                                int.class, rs.getInt("parentBlockId")),
                        //blockId -> FootPrintNum
                        this.jdbcTemplate.queryForObject(getFootPrintNumByBLockIdQuery,
                                int.class, rs.getInt("parentBlockId"))
                )
                ,word);
        return postInfoList;
    }

}