package com.example.demo.src.StampPrint;

import com.example.demo.src.Page.model.GetBlocksRes;
import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.StampPrint.model.GetChildPageBlocksRes;
import com.example.demo.src.StampPrint.model.GetParentDataRes;
import com.example.demo.src.StampPrint.model.PostStampReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StampPrintDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
     *
     * */
    public Boolean stampBlock(PostStampReq postStampReq){
        /** 블럭 복제 */
        // stamp 할 블럭의 내용
        String copyContentQuery = "select content\n" +
                "from Block\n" +
                "where blockId=?;";

        // stamp 한 유저의 My Follow 최상단 페이지 아이디
        String selectCurPageIdQuery = "select p.pageId\n" +
                "from User u, Page p\n" +
                "where u.userId = p.userId and p.status=1\n" +
                "  and p.topOrNot=1 and p.stampOrPrint='S'\n" +
                "  and u.userId = ?;";

        // stamp 한 유저의 My Follow 최상단 페이지에 추가되는 블럭의 orderNum
        String selectOrderNumQuery ="select (select count(*) from Block where status=1 and curPageId=?) as orderNum\n" +
                "from User u, Page p\n" +
                "where u.userId = p.userId and p.status=1\n" +
                "  and p.topOrNot=1 and p.stampOrPrint='S'\n" +
                "  and u.userId = ?;";

        String content = this.jdbcTemplate.queryForObject(copyContentQuery,String.class,postStampReq.getBlockId());
        int curPageId = this.jdbcTemplate.queryForObject(selectCurPageIdQuery,int.class,postStampReq.getFolloweeId());
        int orderNum =  this.jdbcTemplate.queryForObject(selectOrderNumQuery,int.class,postStampReq.getFolloweeId());


        // 블럭 복제
        String createNewBlockQuery = "insert into Block (userId, curPageId, content, orderNum) values(?, ?, ?, ?);";
        Object[] createNewBlockParams = new Object[]{
                postStampReq.getLoginUserId(),curPageId,content,orderNum};
        this.jdbcTemplate.update(createNewBlockQuery,createNewBlockParams);

        String getNewBlockIdQuery = "select blockId from Block where orderNum=?";
        int newBlockId = this.jdbcTemplate.queryForObject(getNewBlockIdQuery, int.class, orderNum);


        /** StampAndPrint stamp 내역 추가 */

        /*
        * -- stamp 과정에 필요한 쿼리

-- new block 의 content (복제)
select content
from Block
where blockId=?;

-- new block 의 curPageId
select p.pageId, (select count(*) from Block where status=1 and curPageId=?) as orderNum
from User u, Page p
where u.userId = p.userId and p.status=1
  and p.topOrNot=1 and p.stampOrPrint='S'
  and u.userId = ?;

-- new block 생성
insert into Block (userId, curPageId, content, orderNum) values(?, ?, ?, ?);

-- originalId 검색
select originalId
from StampAndPrint
where newBlockId=?; -- stamp 할 블럭의 아이디

-- 데이터가 존재하면 그 데이터를 originalId로 사용 / followeeId 는 Req 로 받는 followeeId 사용
-- 데이터가 존재하지 않으면, stamp 할 블럭의 유저 검색 -> originalId, followeeId 둘 다로 사용
select userId
from Block
where blockId=?;

-- StampAndPrint 데이터 추가
insert into StampAndPrint (blockId, originalId, followerId, followeeId, newBlockId, stampOrPrint) values(?, ?, ?, ?, ?, 'S');

        * */

        // 1 이면 sap 데이터가 있는 것. 0 이면 없는 것 stamp 당한 적 없는 것(작성자 본인의 것만 존재)
        String ifStampAndPrintIsNoneQuery = "select exists(select 1 from Block b, StampAndPrint sap where b.blockId=sap.newBlockId and b.blockId = ?);";
        int ifStampAndPrintIsExist = this.jdbcTemplate.queryForObject(ifStampAndPrintIsNoneQuery,
                int.class
                , postStampReq.getBlockId());

        int originalId=0;

        //-- 데이터가 존재하면 그 데이터를 originalId로 사용 / followeeId 는 Req 로 받는 followeeId 사용
        //-- 데이터가 존재하지 않으면, stamp 할 블럭의 유저 검색 -> originalId, followeeId 둘 다로 사용
        if (ifStampAndPrintIsExist == 1){ // 기록이 있음
            String selectOriginalIdQuery = "select originalId\n" +
                    "from StampAndPrint\n" +
                    "where newBlockId=?;";
            originalId = this.jdbcTemplate.queryForObject(selectOriginalIdQuery,int.class, postStampReq.getBlockId());

        }
        else{ // 최초 stamp
            originalId = postStampReq.getFolloweeId();
        }


        // StampAndPrint 에 stamp 내역(?) 추가
        String createStampQuery = "insert into StampAndPrint (blockId, " +
                "originalId, followerId, followeeId, newBlockId, stampOrPrint) " +
                "values(?, ?, ?, ?, ?, 'S');"; // StampAndPrint 에 stamp 내역 추가

        Object[] createNewStampDataParams = new Object[]{
                postStampReq.getBlockId(), originalId, postStampReq.getLoginUserId(), postStampReq.getFolloweeId(), newBlockId};
        this.jdbcTemplate.update(createStampQuery, createNewStampDataParams);


        /** 하위 페이지 탐색 및 복제 */
        duplicateDFS(postStampReq.getBlockId(), newBlockId);

        return true;
    }

    int depth=0;

    public void duplicateDFS(int blockId, int newBlockId){
        String hasChildPageQuery = "select exists(select if(childPageId is null, 0, 1) from Block where blockId=?);";
        int hasChildPage = this.jdbcTemplate.queryForObject(hasChildPageQuery, int.class, blockId);

        if(hasChildPage == 1){// 하위 페이지가 있을 때
            // 하위 페이지의 아이디 - 페이지 내 블럭을 탐색할 때 사용 (curPageId 로 검색)
            String getChildPageIdQuery = "select p.pageId\n" +
                    "from Block b, Page p\n" +
                    "where p.parentBlockId = b.blockId and b.blockId=?;";
            int childPageId = this.jdbcTemplate.queryForObject(getChildPageIdQuery, int.class, blockId);

            // 1. 페이지 복제
            //    1-1. 페이지 생성
            //         - parentPageId, parentBlockId, userId, topOrNot(0 고정), preview, stampOrPrint('S' 고정), depth
            //          새로운 쿼리 - parentPageId, userId, depth / preview
            //    1-2. 페이지 내 블럭 복제 (원래 페이지의 블럭들)
            //         - userId, curPageId(새로 생성된 페이지), childPageId, content(가져와야됨), orderNum
            //          새로운 쿼리 - userId, content
            // 2. 페이지 내 블럭의 하위 페이지 탐색 및 복제 (재귀 ?)


            String getParentDataQuery = "select b.curPageId, b.userId, p.depth\n" +
                    "from Page p, Block b\n" +
                    "where p.pageId = b.curPageId and blockId=?;";

            GetParentDataRes getParentDataRes = this.jdbcTemplate.queryForObject(getParentDataQuery,
                    (rs, rowNum) -> new GetParentDataRes(
                            rs.getInt("curPageId"),
                            rs.getInt("userId"),
                            rs.getInt("depth")
                    ), newBlockId);

            String getPreviewQuery = "select p.preview\n" +
                    "from Page p, Block b\n" +
                    "where p.pageId = b.childPageId and b.blockId=?;";

            String preview = this.jdbcTemplate.queryForObject(getPreviewQuery, String.class, blockId);

            /** 페이지 복제 */
            String duplicatePageQuery = "insert into Page (parentPageId, parentBlockId, userId, topOrNot, preview, stampOrPrint, depth) values (?, ?, ?, 0, ?, 'S', ?);";
            Object[] duplicatePageParams = new Object[]{getParentDataRes.getCurPageId(), newBlockId, getParentDataRes.getUserId(), preview, getParentDataRes.getDepth()+1};
            this.jdbcTemplate.update(duplicatePageQuery, duplicatePageParams);


            /**  페이지 내 블럭 탐색 */
            String getChildBlocksQuery = "select blockId, content, orderNum\n" +
                    "from Block\n" +
                    "where status=1 and curPageId=?;";

            List<GetChildPageBlocksRes> childBlocks = this.jdbcTemplate.query(getChildBlocksQuery,
                    (rs, rowNum) -> new GetChildPageBlocksRes(
                        rs.getInt("blockId"),
                        rs.getString("content"),
                        rs.getInt("orderNum")
            ), newBlockId);

            // TODO dfs 구현..



            /**  페이지 내 블럭 복제 */

            String getNewPageIdQuery = "select pageId\n" +
                    "from Page\n" +
                    "where parentBlockId = ?;";
            int newPageId = this.jdbcTemplate.queryForObject(getNewPageIdQuery, int.class, newBlockId);

            String getContentQuery = "select content \n" +
                    "from Block \n" +
                    "where blockId = ?;";
            String content = this.jdbcTemplate.queryForObject(getContentQuery, String.class, blockId);

            String duplicateBlocksQuery = "insert into Block (userId, curPageId, content, orderNum) values (?, ?, ?, ?);";
            Object[] duplicateBlocksParams = new Object[]{getParentDataRes.getUserId(), newPageId, content, orderNum};
            this.jdbcTemplate.update(duplicateBlocksQuery, duplicateBlocksParams);

        }


        // 하위 페이지가 없을 때, 메소드 종료
    }



    /*
     * stamp 에서 footprint 로 변경할 때
     * */
    public Boolean updateToPrint(int blockId){
        String updateToPrintQuery = "";
        int updateToPrintParams = blockId;
        this.jdbcTemplate.update(deletePageQuery, deletePageParams);

        return true;
    }
}
