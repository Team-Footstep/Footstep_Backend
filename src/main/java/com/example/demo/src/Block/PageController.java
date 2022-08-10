package com.example.demo.src.Block;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Block.model.PostPageReq;
import com.example.demo.src.Block.model.PostPageRes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Pages")
public class PageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private PageProvider pageProvider;
    private PageService pageService;

    @Autowired
    public PageController(PageProvider pageProvider, PageService pageService){
        this.pageProvider = pageProvider;
        this.pageService = pageService;
    }

    @PostMapping("")
    public BaseResponse<PostPageRes> createPage(@RequestBody PostPageReq postPageReq)  {
        //todo validation 처리하기
        try{
            PostPageRes postPageRes = pageService.createPage(postPageReq);
            return new BaseResponse<>(postPageRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
