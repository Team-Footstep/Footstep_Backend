package com.example.demo.src.StampPrint;

import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.StampPrint.model.PostStampReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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



        return true;
    }

    /*
     *
     * */
    public Boolean updateToPrint(int blockId){
        String updateToPrintQuery = "";
        int updateToPrintParams = blockId;
        this.jdbcTemplate.update(deletePageQuery, deletePageParams);

        return true;
    }
}
