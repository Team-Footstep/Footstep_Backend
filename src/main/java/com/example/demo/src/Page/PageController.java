package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Page.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pages") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class PageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    private final PageProvider pageProvider;
    private final PageService pageService;


    @Autowired
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


    /*
     * [POST]
     * 하위 페이지 생성
     * */
    @PostMapping("/create")
    public BaseResponse<PostPageRes> createPage(@RequestBody PostPageReq postPageReq)  {
        //todo validation 처리하기
        try{
            PostPageRes postPageRes = pageService.createPage(postPageReq);
            return new BaseResponse<>(postPageRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /*
     * [PATCH]
     * 페이지 저장 (갱신)
     * */
    @PatchMapping("/save")
    public BaseResponse<PatchPageRes> updatePage(@RequestBody PatchPageReq patchPageReq){
        try{
            PatchPageRes patchPageRes = pageService.updatePage(patchPageReq);
            return new BaseResponse<>(patchPageRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /*
     * [GET]
     * 페이지 진입 시 내용 가져오기
     * */
    @GetMapping("/get/{pageId}")
    public BaseResponse<GetPageRes> getPage(@PathVariable int pageId)  {
        //todo validation 처리하기
        try{
            GetPageRes getPageRes = pageProvider.retrievePage(pageId);
            return new BaseResponse<>(getPageRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
