package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.Comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.hibernate.sql.InFragment.NULL;

@Service

public class CommentService {
    private final CommentDao commentDao;

    @Autowired
    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public PostCommentRes createComment(int pageId, int blockId, PostCommentReq postCommentReq) throws BaseException {

        PostCommentRes postCommentRes = commentDao.createComment(pageId, blockId, postCommentReq);
        return postCommentRes;
    }

    public PatchCommentRes modifyComment(int pageId, int blockId, PatchCommentReq patchCommentReq) throws BaseException {


        PatchCommentRes patchCommentRes = commentDao.modifyComment(pageId, blockId, patchCommentReq);
        return patchCommentRes;

    }
    public DeleteCommentRes deleteComment(DeleteCommentReq deleteCommentReq, int commentId) throws BaseException {

        DeleteCommentRes deleteCommentRes = commentDao.deleteComment(deleteCommentReq, commentId);
        return deleteCommentRes;
    }

    //삭제할 댓글이 존재하는지 확
    public int checkCommentExist(int commentId) {
        int checkCommentId = commentDao.checkCommentExist(commentId);
        return checkCommentId;
    }
    //이미 삭제되었는지 확인
    public int checkCommentStatus(int commentId) {
        int checkCommentNull = commentDao.checkCommentStatus(commentId);
        return checkCommentNull;
    }
    //해당 페이지-블록에 댓글이 존재하는지 확인
    public int checkCommentInPageBlock(int pageId, int blockId) {
        int checkCommentInPageBlock = commentDao.checkCommentInPageBlock(pageId, blockId);
        return checkCommentInPageBlock;
    }
    //해당 페이지-블록에 댓글이 비어있는지
    public int commentContent(int pageId, int blockId) {
        int commentContent = commentDao.commentContentExists(pageId, blockId);
        return commentContent;
    }
}
