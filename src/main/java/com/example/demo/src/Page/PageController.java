package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Page.model.PatchAccessReq;
import com.example.demo.src.Page.model.PatchBookmarkReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    * 페이지 공개/미공개 설정
    * */
    @ResponseBody
    @PatchMapping("/access")
    public BaseResponse<Boolean> getNewFootsteps(@RequestBody PatchAccessReq patchAccessReq) {
        try{
            pageService.updateAccess(patchAccessReq);
            return new BaseResponse<>(true);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
     * [PATCH]
     * 페이지 북마크 설정/해제
     * */
    @ResponseBody
    @PatchMapping("/bookmark")
    public BaseResponse<Boolean> getNewFootsteps(@RequestBody PatchBookmarkReq patchBookmarkReq) {
        try{
            pageService.updateBookmark(patchBookmarkReq);
            return new BaseResponse<>(true);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}