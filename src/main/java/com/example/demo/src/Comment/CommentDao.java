package com.example.demo.src.Comment;

import com.example.demo.src.Bookmark.model.GetBookmarksRes;
import com.example.demo.src.Comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.valueOf;

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

    public PatchCommentRes modifyComment(int pageId, int blockId, PatchCommentReq patchCommentReq) {
        String modifyCommentQuery = "UPDATE Comment set content = ? where pageId = ? and blockId = ?;";
        Object[] modifyCommentParams = new Object[]{patchCommentReq.getContent(), pageId, blockId};
        this.jdbcTemplate.update(modifyCommentQuery, modifyCommentParams);

        return new PatchCommentRes(patchCommentReq.getUserId(), pageId, blockId, patchCommentReq.getContent(), LocalDateTime.now());
    }

    public DeleteCommentRes deleteComment(DeleteCommentReq deleteCommentReq, int commentId) {
        String deleteCommentQuery = "UPDATE Comment set content = null, status = 0 where commentId = ?";
        Object[] deleteCommentParams = new Object[]{commentId};
        this.jdbcTemplate.update(deleteCommentQuery, deleteCommentParams);
        return new DeleteCommentRes(deleteCommentReq.getPageId(), deleteCommentReq.getBlockId(), null, 0);

    }
    public List<GetCommentRes> getComment(int pageId, int blockId) {
        String getCommentQuery = "select C.commentId ,C.pageId, C.blockId,  C.userId, U.userName, C.content, U.userImgUrl, C.updatedAt\n" +
                "from Comment C\n" +
                "inner join User U on C.userId = U.userId\n" +
                "inner join (select pageId from Page where status = 1 and access =1)as P  on C.pageId = P.pageId\n" +
                "where C.pageId = ? and C.blockId = ? and C.status = 1";
        Object[] getCommentParams = new Object[]{pageId, blockId};
        return this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getInt("commentId"),
                        rs.getInt("pageId"),
                        rs.getInt("blockId"),
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("userImgUrl"),
                        rs.getString("content"),
                        rs.getString("updatedAt")

                ), getCommentParams);
    }



    public int checkCommentExist(int commentId){
        String checkCommentQuery = "select exists(select commentId from Comment where commentId = ?)";
        int checkCommentParams = commentId;
        return this.jdbcTemplate.queryForObject(checkCommentQuery, int.class, checkCommentParams);

    }

    //이미 삭제되었는지
    public int checkCommentStatus(int commentId) {
        String checkCommentStatusQuery = "select status from Comment where commentId = ?";
        int checkCommentStatusParams =commentId;
        return this.jdbcTemplate.queryForObject(checkCommentStatusQuery, int.class, checkCommentStatusParams);
    }

    public int checkCommentInPageBlock(int pageId, int blockId) {
        String checkCommentInPageBlockQuery = "select exists(select commentId from Comment where pageId = ? and blockId = ?)";
        Object[] checkCommentInPageBlockParams = new Object[]{pageId, blockId};
        return this.jdbcTemplate.queryForObject(checkCommentInPageBlockQuery, int.class, checkCommentInPageBlockParams);
    }
}
