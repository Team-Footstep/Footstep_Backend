package com.example.demo.src.Page;

import com.example.demo.src.Page.model.PatchAccessReq;
import com.example.demo.src.Page.model.PatchBookmarkReq;
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

    public void updateAccess(PatchAccessReq patchAccessReq){
        int updateAccessPageIdParams = patchAccessReq.getPageId();
        int updateAccess = patchAccessReq.getAccess();


        String updateAccessQuery = "update Page\n" +
                "set access=" + Integer.toString(updateAccess) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateAccessQuery, updateAccessPageIdParams);
    }

    public void updateBookmark(PatchBookmarkReq patchBookmarkReq){
        int updateBookmarkPageIdParams = patchBookmarkReq.getPageId();
        int updateBookmark = patchBookmarkReq.getBookmark();


        String updateBookmarkQuery = "update Page\n" +
                "set bookmark=" + Integer.toString(updateBookmark) + "\n" +
                "where pageId=?;";

        this.jdbcTemplate.update(updateBookmarkQuery, updateBookmarkPageIdParams);
    }
}


