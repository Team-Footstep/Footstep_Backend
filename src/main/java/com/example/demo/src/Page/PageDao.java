package com.example.demo.src.Page;


import com.example.demo.src.Page.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public class PageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //page 생성

    /**
     * 페이지 저장 및 수정
     * @author nnlnuu
     */
    public PatchPageRes updatePage(PatchPageReq patchPageReq) {

        //새로 페이지 정보 처리
        String updatePageQuery = "update Page set preview =?,status =? ,stampOrPrint = ?, bookmark =?,\n" +
                "                access = ?\n" +
                "where pageId = ?";
        Object[] updatePageParams = {patchPageReq.getPreview(), patchPageReq.getStatus(),
                patchPageReq.getStampOrPrint(), patchPageReq.getBookmark(),
                patchPageReq.getAccess(), patchPageReq.getPageId()};

        String getPageIdQuery = "select *\n" +
                "from Page\n" +
                "where pageId = ?";

        this.jdbcTemplate.update(updatePageQuery, updatePageParams);

        // 새로운 or 기존 블록 정보 처리
        String createBlockQuery = "insert into Block (userId, curPageId, childPageId, content, isNewBlock, orderNum, status) " +
                "VALUES(?,?,?,?,?,?,?);";
        String updateBlockQuery = "update Block set childPageId = ?,content=?,isNewBlock=?,orderNum=?,status=?\n" +
                "where blockId = ?";
        List<GetContentsRes> contents = patchPageReq.getContentList();
        for(int i =0;i<contents.size();i++){
            if(checkNewBlock(contents.get(i).getIsNewBlock()))// new block이면 -> create
            {
                GetContentsRes c = contents.get(i);
                Object[] createBlockParams ={c.getUserId(),c.getCurPageId(),
                        c.getChildPageId(),c.getContent(),0,i,c.getStatus()
                };
                this.jdbcTemplate.update(createBlockQuery,createBlockParams);
            }else{// 아니면 update
                GetContentsRes c = contents.get(i);
                Object[] updateBlockParams ={
                        c.getChildPageId(),c.getContent(),0,i,c.getStatus(),c.getBlockId()};
                this.jdbcTemplate.update(updateBlockQuery, updateBlockParams);
            }
        }

        //응답 객체
        PatchPageRes patchPageRes = this.jdbcTemplate.queryForObject(getPageIdQuery,
                (rs, num) -> new PatchPageRes(
                        rs.getInt("pageId"),
                        rs.getTimestamp("updatedAt"),
                        "페이지가 업데이트 되었습니다."
                ),patchPageReq.getPageId()
        );
        return patchPageRes;
    }

    /**
     * 하위 페이지 생성
     * @author nnlnuu
     */
    public PostPageRes createPage(PostPageReq postPageReq) {
        String createPageQuery = "insert into Page (parentPageId, parentBlockId," +
                " userId, topOrNot, status, stampOrPrint,depth) VALUES(?,?,?,?,?,?,?)";


        // 깊이는 따로 구해주기
        String getParentPageDepthQuery = "select depth\n" +
                "from Page\n" +
                "where pageId = ?"; // 여기에 들어갈 정보 -> depth
        @NotNull
        int depth = this.jdbcTemplate.queryForObject(getParentPageDepthQuery,int.class,postPageReq.getParentPageId());

        // page date 생성 인자들
        Object[] createPageParams = new Object[]{postPageReq.getParentPageId(), postPageReq.getParentBlockId(),postPageReq.getUserId()
                , postPageReq.isTopOrNot(), postPageReq.getStatus(),postPageReq.getStampOrPrint(),(depth+1)
        };

        // 페이지 생성 구문
        this.jdbcTemplate.update(createPageQuery, createPageParams);

        // pageRes 처리를 위한 새로 만들어진 PageId
        String getPageResByPageIdQuery = "select pageId,createdAt,status\n" +
                "from Page " +
                "where pageId = ?";
        String getPageIdQuery ="select pageId from Page where parentBlockId = ?";
        @NotNull
        int curPageId = this.jdbcTemplate.queryForObject(getPageIdQuery,int.class,postPageReq.getParentBlockId());

        // 페이지가 생성되었을때 block 데이터에서도 childPageId 변경해줘야 함
        String updateParentBlockQuery = "update Block set childPageId = ?\n" +
                "where blockId  = ?";
        //현재 생성된 페이지의 Id를 -> parentBlock의 childPageId에 update
        Object[] updateParentBlockParams = new Object[] {
                curPageId,postPageReq.getParentBlockId() };

        this.jdbcTemplate.update(updateParentBlockQuery,updateParentBlockParams);

        //PostPageRes 반환
        return this.jdbcTemplate.queryForObject(getPageResByPageIdQuery,
                (rs,num) -> new PostPageRes(
                        rs.getInt("pageId"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"))

                ,curPageId);
    }

    public boolean checkExist(int parentBlockId) {
        String checkParentBlockIdQuery = "select count(*) from Page where parentBlockId = ?";
        int check = this.jdbcTemplate.queryForObject(checkParentBlockIdQuery,int.class,parentBlockId);
        if(check>=1) {
            return true;
        }else return false;
    }

    /**
     * 새로 생성된 블록인지 확인 validation
     * @author nnlnuu
     */
    public boolean checkNewBlock(int newBlock){
        if(newBlock==1) return true;
        else return false;
    }

    /**
     * 페이지 깊이 확인 validation
     * @author nnlnuu
     */
    public boolean checkDepth(int pageId) {
        String checkPageDepthQuery = "select depth from Page where pageId = ?";
        int depth = this.jdbcTemplate.queryForObject(checkPageDepthQuery,int.class,pageId);
        if(depth>=15) return false;
        else return true;
    }


    /**
     * 공개/미공개 수정
     * @author ro-el
     * */
    public void updateAccess(PatchAccessReq patchAccessReq){
        int updateAccessPageIdParams = patchAccessReq.getPageId();
        int updateAccess = patchAccessReq.getAccess();


        String updateAccessQuery = "update Page\n" +
                "set access=" + Integer.toString(updateAccess) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateAccessQuery, updateAccessPageIdParams);
    }

    /**
     * 페이지 북마크 설정/해제
     * @author ro-el
     * */
    public void updateBookmark(PatchBookmarkReq patchBookmarkReq){
        int updateBookmarkPageIdParams = patchBookmarkReq.getPageId();
        int updateBookmark = patchBookmarkReq.getBookmark();


        String updateBookmarkQuery = "update Page\n" +
                "set bookmark=" + Integer.toString(updateBookmark) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateBookmarkQuery, updateBookmarkPageIdParams);
    }


    /**
     * 페이지 진입 시 내용 가져오기
     * @author ro-el
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

    /**
     * 최초 작성자, 상위 작성자 트랙킹
     * 표시되지 않는 경우 예외처리
     * @author ro-el
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

    /**
     * 없는 페이지인 경우 validation
     * @author ro-el
     * */
    public int checkPageExist(int pageId){
        String checkPageAccessQuery = "select exists(select 1 from Page where pageId = ?)";
        int checkPageAccessParams = pageId;
        return this.jdbcTemplate.queryForObject(checkPageAccessQuery,
                int.class,
                checkPageAccessParams);
    }

    /**
     * 미공개 페이지인 경우 validation
     * @author ro-el
     * */
    public int checkPageAccess(int pageId){
        String checkPageAccessQuery = "select exists(select 1 from Page where access=1 and pageId = ?)";
        int checkPageAccessParams = pageId;
        return this.jdbcTemplate.queryForObject(checkPageAccessQuery,
                int.class,
                checkPageAccessParams);
    }


}
