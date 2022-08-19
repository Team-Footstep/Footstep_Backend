package com.example.demo.src.Page;


import com.example.demo.src.Page.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //page 생성
    public PostPageRes createPage(PostPageReq postPageReq) {
        String createPageQuery = "insert into Page (parentPageId,parentBlockId,userId,topOrNot,access,stampOrPrint,preview)\n" +
                "values (?,?,?,?,?,?,?)";
        String getPageResByPageIdQuery = "select userId,pageId,createdAt,status " +
                "from Page " +
                "where pageId = ?";
        String getPageIdQuery ="select pageId from Page where parentBlockId = ?";
        Object[] createPageParams = new Object[]{postPageReq.getParentPageId(), postPageReq.getParentBlockId()
                , postPageReq.isTopOrNot(), postPageReq.getAccess(), postPageReq.getStampOrPrint(), postPageReq.getPreview()
        };
        // 페이지 생성 구문
        this.jdbcTemplate.update(createPageQuery, createPageParams);

        int PageId = this.jdbcTemplate.queryForObject(getPageIdQuery,int.class,postPageReq.getParentBlockId());

        return this.jdbcTemplate.queryForObject(getPageResByPageIdQuery,
                (rs,num) -> new PostPageRes(
                        rs.getInt("pageId"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"))
                ,PageId);
    }

    public PatchPageRes updatePage(PatchPageReq patchPageReq) {
        String updatePageQuery = "update Page set preview =?,status =? ,stampOrPrint = ?, bookmark =?,\n" +
                "                access = ?\n" +
                "where pageId = ?";
        // todo : updateBlockQuery 작성해주기
        String updateBlockQuery = "";
        Object[] updatePageParams = {patchPageReq.getPreview(), patchPageReq.getStatus(),
                patchPageReq.getStampOrPrint(), patchPageReq.getBookmark(),
                patchPageReq.getAccess(), patchPageReq.getPageId()};

        String getPageIdQuery = "select *\n" +
                "from Page\n" +
                "where pageId = ?";

        List<GetContentsRes> contents = patchPageReq.getContentList();
        // enhanced for loop
        for( GetContentsRes c: contents){
          Object[]  updateBlockParams = { c.getChildPageId(),c.getContent(),c.getOrderNum(),c.getStatus()};
            // 총 i 번 업데이트
            this.jdbcTemplate.update(updateBlockQuery, updateBlockParams);
        }
//        for (int i = 0; i < contents.size(); i++) {
//            Object[] updateBlockParams = {
//                    contents.get(i).getChildPageId(), contents.get(i).getContent(),
//                    contents.get(i).getOrderNum(), contents.get(i).getStatus()
//            };
//            // 총 i 번 업데이트
//            this.jdbcTemplate.update(updateBlockQuery, updateBlockParams);
//        }
        this.jdbcTemplate.update(updatePageQuery, updatePageParams);

        //todo : 인자 추가
        PatchPageRes patchPageRes = this.jdbcTemplate.queryForObject(getPageIdQuery,
                (rs, num) -> new PatchPageRes(
                        rs.getInt("pageId"),
                        rs.getTimestamp("updatedAt"),
                        "페이지가 업데이트 되었습니다."
                ),patchPageReq.getPageId()
        );
        return patchPageRes;
    }

    /*
     * 공개/미공개 수정
     * */
    public void updateAccess(PatchAccessReq patchAccessReq){
        int updateAccessPageIdParams = patchAccessReq.getPageId();
        int updateAccess = patchAccessReq.getAccess();


        String updateAccessQuery = "update Page\n" +
                "set access=" + Integer.toString(updateAccess) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateAccessQuery, updateAccessPageIdParams);
    }

    /*
     * 페이지 북마크 설정/해제
     * */
    public void updateBookmark(PatchBookmarkReq patchBookmarkReq){
        int updateBookmarkPageIdParams = patchBookmarkReq.getPageId();
        int updateBookmark = patchBookmarkReq.getBookmark();


        String updateBookmarkQuery = "update Page\n" +
                "set bookmark=" + Integer.toString(updateBookmark) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateBookmarkQuery, updateBookmarkPageIdParams);
    }


    /*
     * 페이지 진입 시 내용 가져오기
     * */
    public GetPageRes retrievePage(int pageId){
        int retrievePageParams = pageId;

        // 최상단 페이지인지 판단
        String isTopPageQuery = "select topOrNot from Page where pageId = ?";
        int isTopPage = this.jdbcTemplate.queryForObject(isTopPageQuery,
                int.class
                , retrievePageParams);


        // 페이지 내용 쿼리 - 최상단 페이지인 경우
        String retrieveTopPageQuery = "select pageId, topOrNot, preview, access, bookmark\n" +
                "from Page\n" +
                "where pageId=?;\n";

        // 페이지 내용 쿼리 - 최상단 페이지가 아닌 경우
        String retrievePageQuery = "select p.pageId, p.topOrNot, p.preview, b.blockId as parentBlockId, b.content as parentBlockContent, p.access, p.bookmark\n" +
                "from Page p, Block b\n" +
                "where p.pageId = b.childPageId\n" +
                "  and p.pageId=?;\n";

        // 페이지 내 블럭들 쿼리
        String retrievePageBlocksQuery = "select b.blockId, b.content, b.childPageId, b.status\n" +
                "from Page p, Block b\n" +
                "where p.pageId = b.curPageId and b.status=1\n" +
                "  and p.pageId=?\n" +
                "order by b.orderNum;";

        // 블럭이 stamp 당한 횟수
        String getStampNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'S'\n" +
                "  and sap.blockId=?;";

        // 블럭이 footprint 당한 횟수
        String getFootprintNumQuery = "select count(*)\n" +
                "from StampAndPrint sap\n" +
                "where sap.status=1\n" +
                "  and sap.stampOrPrint = 'P'\n" +
                "  and sap.blockId=?;";

        if (isTopPage==1){
            return this.jdbcTemplate.queryForObject(retrieveTopPageQuery,
                    (rs, rowNum) -> new GetPageRes(
                            rs.getInt("pageId"),
                            rs.getInt("topOrNot"),
                            rs.getString("preview"),
                            0,
                            null,
                            rs.getInt("access"),
                            rs.getInt("bookmark"),
                            jdbcTemplate.query(retrievePageBlocksQuery,
                                    (rk, rowNum_k) -> new GetBlocksRes(
                                            rk.getInt("blockId"),
                                            rk.getString("content"),
                                            rk.getInt("childPageId"),
                                            originalFollowee(rk.getInt("blockId")),   // originalId, followeeId가 0일 경우 프론트에서 표시 X
                                            rk.getInt("status"),
                                            jdbcTemplate.queryForObject(getStampNumQuery,
                                                    int.class
                                                    , rk.getInt("blockId")),
                                            jdbcTemplate.queryForObject(getFootprintNumQuery,
                                                    int.class
                                                    , rk.getInt("blockId")))
                                    ,retrievePageParams)
                    ), retrievePageParams);
        }
        else{
            return this.jdbcTemplate.queryForObject(retrievePageQuery,
                    (rs, rowNum) -> new GetPageRes(
                            rs.getInt("pageId"),
                            rs.getInt("topOrNot"),
                            rs.getString("preview"),
                            rs.getInt("parentBlockId"),
                            rs.getString("parentBlockContent"),
                            rs.getInt("access"),
                            rs.getInt("bookmark"),
                            jdbcTemplate.query(retrievePageBlocksQuery,
                                    (rk, rowNum_k) -> new GetBlocksRes(
                                            rk.getInt("blockId"),
                                            rk.getString("content"),
                                            rk.getInt("childPageId"),
                                            originalFollowee(rk.getInt("blockId")),   // originalId, followeeId가 0일 경우 프론트에서 표시 X
                                            rk.getInt("status"),
                                            jdbcTemplate.queryForObject(getStampNumQuery,
                                                    int.class
                                                    , rk.getInt("blockId")),
                                            jdbcTemplate.queryForObject(getFootprintNumQuery,
                                                    int.class
                                                    , rk.getInt("blockId")))
                                    ,retrievePageParams)
                    ), retrievePageParams);

        }

    }

    /*
     * 최초 작성자, 상위 작성자 트랙킹
     * 표시되지 않는 경우 예외처리
     * */
    public GetOriginalFolloweeRes originalFollowee(int blockId){
        int originalFolloweeParams = blockId;
        GetOriginalFolloweeRes getOriginalFolloweeRes = new GetOriginalFolloweeRes(0,0);

        // 1 이면 sap 데이터가 있는 것. 0 이면 없는 것 stamp 당한 적 없는 것(작성자 본인의 것만 존재)
        String ifStampAndPrintIsNoneQuery = "select exists(select 1 from Block b, StampAndPrint sap where b.blockId=sap.newBlockId and b.blockId = ?);";
        int ifStampAndPrintIsNone = this.jdbcTemplate.queryForObject(ifStampAndPrintIsNoneQuery,
                int.class
                , originalFolloweeParams);

        /*
        String IfOriginalIdIsMeQuery = "select if(b.userId = sap.originalId, 1, 0)\n" +
                "from Block b, StampAndPrint sap\n" +
                "where b.blockId = sap.newBlockId\n" +
                "and sap.status=1\n" +
                "and b.blockId=?;";
        */

        if (ifStampAndPrintIsNone == 1){
            /*
            int ifOriginalIdIsMe = this.jdbcTemplate.queryForObject(IfOriginalIdIsMeQuery,
                    int.class
                    , originalFolloweeParams);

            if(ifOriginalIdIsMe == 0){
            }
            */

            String getOriginalFolloweeIdQuery = "select sap.originalId, sap.followeeId\n" +
                    "from Block b, StampAndPrint sap\n" +
                    "where b.blockId = sap.newBlockId\n" +
                    "  and sap.status=1\n" +
                    "  and b.blockId=?;";

            getOriginalFolloweeRes = this.jdbcTemplate.queryForObject(getOriginalFolloweeIdQuery,
                    (rs, rowNum) -> new GetOriginalFolloweeRes(
                            rs.getInt("originalId"),
                            rs.getInt("followeeId")
                    ), originalFolloweeParams);

        }

        return getOriginalFolloweeRes;
    }

    /*
     * 없는 페이지인 경우 validation
     * */
    public int checkPageExist(int pageId){
        String checkPageAccessQuery = "select exists(select 1 from Page where pageId = ?)";
        int checkPageAccessParams = pageId;
        return this.jdbcTemplate.queryForObject(checkPageAccessQuery,
                int.class,
                checkPageAccessParams);
    }

    /*
     * 미공개 페이지인 경우 validation
     * */
    public int checkPageAccess(int pageId){
        String checkPageAccessQuery = "select exists(select 1 from Page where access=1 and pageId = ?)";
        int checkPageAccessParams = pageId;
        return this.jdbcTemplate.queryForObject(checkPageAccessQuery,
                int.class,
                checkPageAccessParams);
    }


}
