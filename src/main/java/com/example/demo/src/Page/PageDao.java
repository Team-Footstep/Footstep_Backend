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
    public PostPageRes createPage(PostPageReq postPageReq) {
        String createPageQuery = "insert into Page (parentPageId, parentBlockId," +
                " userId, topOrNot, status, stampOrPrint,depth) VALUES(?,?,?,?,?,?,?)";

        String getParentPageDepthQuery = "select depth\n" +
                "from Page\n" +
                "where pageId = ?"; // 여기에 들어갈 정보 -> depth
        // 깊이 따로 조회
        @NotNull
        int depth = this.jdbcTemplate.queryForObject(getParentPageDepthQuery,int.class,postPageReq.getParentPageId());
        String getPageResByPageIdQuery = "select pageId,createdAt,status\n" +
                "from Page " +
                "where pageId = ?";

        String getPageIdQuery ="select pageId from Page where parentBlockId = ?";


        Object[] createPageParams = new Object[]{postPageReq.getParentPageId(), postPageReq.getParentBlockId(),postPageReq.getUserId()
                , postPageReq.isTopOrNot(), postPageReq.getStatus(),postPageReq.getStampOrPrint(),(depth+1)
        };

        // 페이지 생성 구문
        this.jdbcTemplate.update(createPageQuery, createPageParams);

        @NotNull
        int curPageId = this.jdbcTemplate.queryForObject(getPageIdQuery,int.class,postPageReq.getParentBlockId());

        String updateParentBlockQuery = "update Block set childPageId = ?\n" +
                "where blockId  = ?";
        Object[] updateParentBlockParams = new Object[] {
                curPageId,postPageReq.getParentBlockId() };
        this.jdbcTemplate.update(updateParentBlockQuery,updateParentBlockParams);
        return this.jdbcTemplate.queryForObject(getPageResByPageIdQuery,
                (rs,num) -> new PostPageRes(
                        rs.getInt("pageId"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"))
                ,curPageId);
    }

    public PatchPageRes updatePage(PatchPageReq patchPageReq) {
        String updatePageQuery = "update Page set preview =?,status =? ,stampOrPrint = ?, bookmark =?,\n" +
                "                access = ?\n" +
                "where pageId = ?";


        String updateBlockQuery = "update Block set childPageId= ?, content = ?,orderNum=?,status=?\n" +
                "where curPageId = ?";
        Object[] updatePageParams = {patchPageReq.getPreview(), patchPageReq.getStatus(),
                patchPageReq.getStampOrPrint(), patchPageReq.getBookmark(),
                patchPageReq.getAccess(), patchPageReq.getPageId()};

        String getPageIdQuery = "select *\n" +
                "from Page\n" +
                "where pageId = ?";

        List<GetContentsRes> contents = patchPageReq.getContentList();

        int index = 1;
        for( GetContentsRes c: contents){
          Object[]  updateBlockParams = { c.getChildPageId(),c.getContent(),index++,c.getStatus(),patchPageReq.getPageId()};
            // 총 i 번 업데이트
            this.jdbcTemplate.update(updateBlockQuery, updateBlockParams);
        }
        this.jdbcTemplate.update(updatePageQuery, updatePageParams);

        //todo : 데이터 잘 저장 되는지 점검
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

    public boolean checkExist(int parentBlockId) {
        String checkParentBlockIdQuery = "select count(*) from Page where parentBlockId = ?";
        int check = this.jdbcTemplate.queryForObject(checkParentBlockIdQuery,int.class,parentBlockId);
        if(check>=1) {
            return true;
        }else return false;
    }
}



