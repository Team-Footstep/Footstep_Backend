package com.example.demo.src.Comment;

import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentProvider {
    private final CommentDao commentDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public CommentProvider(CommentDao commentDao) {
        this.commentDao = commentDao;
    }
}
