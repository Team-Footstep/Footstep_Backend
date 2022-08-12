package com.example.demo.src.Comment;

import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.Comment.model.PostCommentRes;
import com.example.demo.src.User.model.GetProfileRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class CommentDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostCommentRes createComment(int pageId,int blockId, PostCommentReq commentReq) {
        String createCommentQuery = "INSERT INTO Comment (content, status, userId, pageId, blockId) values (?, 1, ?, ?, ?);";
        Object[] createCommentParams = new Object[]{commentReq.getContent(),commentReq.getUserId(), pageId, blockId};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        return new PostCommentRes(commentReq.getUserId(), pageId, blockId, commentReq.getContent(), 1, LocalDateTime.now());
    }

}
