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

    public boolean checkExist(int parentBlockId) {
        String checkParentBlockIdQuery = "select count(*) from Page where parentBlockId = ?";
        int check = this.jdbcTemplate.queryForObject(checkParentBlockIdQuery,int.class,parentBlockId);
        if(check>=1) {
            return true;
        }else return false;
    }

    public boolean checkNewBlock(int newBlock){
        if(newBlock==1) return true;
        else return false;
    }
}



