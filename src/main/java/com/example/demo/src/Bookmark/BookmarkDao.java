package com.example.demo.src.Bookmark;

import com.example.demo.src.Bookmark.model.GetBookmarksRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BookmarkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetBookmarksRes> getBookmarks(int userId){
        int getBookmarksParams = userId;

        String getBookmarksQuery = "select userId, pageId, preview, stampOrPrint\n" +
                "from Page\n" +
                "where bookmark=1\n" +
                "    and status=1\n" +
                "    and topOrNot=0\n" +
                "    and access=1\n" +
                "    and userId=?";

        return this.jdbcTemplate.query(getBookmarksQuery,
                (rs, rowNum) -> new GetBookmarksRes(
                        rs.getInt("userId"),
                        rs.getInt("pageId"),
                        rs.getString("preview"),
                        rs.getString("stampOrPrint")
                ), getBookmarksParams);
    }

    public int checkUserExist(int userId){
        String checkUserExistQuery = "select exists(select userId from User where userId = ?)";
        int checkUserExistParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
}


