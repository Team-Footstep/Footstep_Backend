package com.example.demo.src.Page;

<<<<<<< HEAD
import com.example.demo.src.Page.model.GetPageRes;
=======

import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.Page.model.PostPageRes;
>>>>>>> nnlnuu
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> nnlnuu

@Repository
public class PageDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

<<<<<<< HEAD
=======
    //page 생성
    public PostPageRes createPage(PostPageReq postPageReq) {
        String createPageQuery = "insert into Page (parentPageId,parentBlockId,userId,topOrNot,access,stampOrPrint,preview)\n" +
                "values (?,?,?,?,?,?,?)";
        String getPageResByPageIdQuery = "select userId,pageId,createdAt,status\n" +
                "from Page\n" +
                "where pageId = ?";
        String getPageIdQuery ="select pageId\n" +
                "from Page\n" +
                "where parentBlockId = ?";

        Object[] createPageParams = new Object[]{postPageReq.getParentPageId(), postPageReq.getParentBlockId(), postPageReq.getUserId()
                , postPageReq.isTopOrNot(), postPageReq.getAccess(), postPageReq.getStampOrPrint(), postPageReq.getPreview()
        };
        // 페이지 생성 구문
        this.jdbcTemplate.update(createPageQuery, createPageParams);


        int PageId = this.jdbcTemplate.queryForObject(getPageIdQuery,int.class,postPageReq.getParentBlockId());

        return this.jdbcTemplate.queryForObject(getPageResByPageIdQuery,
                (rs,num) -> new PostPageRes(
                        rs.getInt("userId"),
                        rs.getInt("pageId"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status"))
                ,PageId);
    }
>>>>>>> nnlnuu

}


<<<<<<< HEAD
=======









>>>>>>> nnlnuu
