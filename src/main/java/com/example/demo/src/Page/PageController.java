package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Page.model.GetPageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pages") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class PageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PageProvider pageProvider;
    @Autowired
    private final PageService pageService;


    public PageController(PageProvider pageProvider, PageService pageService){
        this.pageProvider = pageProvider;
        this.pageService = pageService;
    }

    /*
    * [PATCH]
    * 공개 설정
    * */


    /*
     * [PATCH]
     * 미공개 설정
     * */

}