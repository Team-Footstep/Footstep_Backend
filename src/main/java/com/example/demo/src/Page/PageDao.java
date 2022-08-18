package com.example.demo.src.Page;


import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
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

}
