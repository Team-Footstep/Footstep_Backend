package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class PageProvider {

    private final PageDao pageDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PageProvider(PageDao pageDao) {
        this.pageDao = pageDao;
    }

}

