package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.Comment.model.PostCommentRes;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.POST_USERS_EMPTY_USERNAME;

@Service

public class CommentService {
    private final CommentDao commentDao;
    @Autowired
    public CommentService(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public PostCommentRes createComment(int pageId,int blockId ,PostCommentReq postCommentReq) {
        //TODO : 댓글 내용이 비어있을떄 처리해주기
//        if(postCommentReq.getContent() == null){
//            return;
//        }
        PostCommentRes postCommentRes = commentDao.createComment(pageId, blockId, postCommentReq);
        return  postCommentRes;
    }
}
