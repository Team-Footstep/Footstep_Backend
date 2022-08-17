package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.Comment.model.GetCommentRes;
import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CommentProvider {
    private final CommentDao commentDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public CommentProvider(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
    public List<GetCommentRes> getComment(int pageId, int blockId) throws BaseException {
        try{
            return commentDao.getComment(pageId, blockId);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
