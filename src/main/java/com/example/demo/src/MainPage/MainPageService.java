package com.example.demo.src.MainPage;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class MainPageService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MainPageDao mainPageDao;
    private final MainPageProvider mainPageProvider;


    @Autowired
    public MainPageService(MainPageDao mainPageDao, MainPageProvider mainPageProvider) {
        this.mainPageDao = mainPageDao;
        this.mainPageProvider = mainPageProvider;
    }

}