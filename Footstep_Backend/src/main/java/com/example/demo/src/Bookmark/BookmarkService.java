package com.example.demo.src.Bookmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class BookmarkService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookmarkDao bookmarkDao;
    private final BookmarkProvider bookmarkProvider;


    @Autowired
    public BookmarkService(BookmarkDao bookmarkDao, BookmarkProvider bookmarkProvider) {
        this.bookmarkDao = bookmarkDao;
        this.bookmarkProvider = bookmarkProvider;
    }

}