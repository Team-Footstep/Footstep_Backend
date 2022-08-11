package com.example.demo.src.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class PageService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PageDao pageDao;
    private final PageProvider pageProvider;


    @Autowired
    public PageService(PageDao pageDao, PageProvider pageProvider) {
        this.pageDao = pageDao;
        this.pageProvider = pageProvider;
    }

}