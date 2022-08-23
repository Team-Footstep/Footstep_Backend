package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
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


    /**
    * [PATCH] /pages/access
    * 페이지 공개/미공개 설정
    * @param patchAccessReq
    * @return BaseResponse<Boolean>
    * @author ro-el
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


    /**
     * [PATCH] /pages/bookmark
     * 페이지 북마크 설정/해제
     * @param patchBookmarkReq
     * @return BaseResponse<Boolean>
     * @author ro-el
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

    /**
     * 하위 페이지 생성 api
     * [Post] /pages/create
     * @return BaseResponse<PostPageRes>
     * @author nnlnuu
     * */
    @PostMapping("/create")
    public BaseResponse<PostPageRes> createPage(@RequestBody PostPageReq postPageReq) {

        try{
            //todo : 하위 페이지 중복되지 않는지 검증
            if(pageService.checkExist(postPageReq.getParentBlockId()))
                return new BaseResponse<>(BaseResponseStatus.POST_FAIL_PAGE);
            else if(pageService.checkDepth(postPageReq.getParentPageId())) { // 깊이가 15 이상일땐 페이지 생성 X
                throw new BaseException(BaseResponseStatus.OVER_PAGE_ERROR);
            }
            else{
                PostPageRes postPageRes = pageService.createPage(postPageReq);
                return new BaseResponse<>(postPageRes);
            }
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 페이지 저장 api
     * [Patch] /pages/save
     * @return BaseResponse<PatchPageRes>
     * @author nnlnuu
     * */
    @PutMapping("save")
    public BaseResponse<PutPageRes> savePage(@RequestBody PutPageReq patchPageReq){
        try{
            PutPageRes patchPageRes = pageService.updatePage(patchPageReq);
            return new BaseResponse<>(patchPageRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * [GET] /pages/get/{pageId}
     * 페이지 진입 시 내용 가져오기
     * @param pageId
     * @return BaseResponse<GetPageRes>
     * @author ro-el
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

    /**
     * [DELETE]
     * (하위)페이지 삭제
     * */
    @DeleteMapping("/delete/{pageId}")
    public BaseResponse<Boolean> deletePage(@PathVariable int pageId)  {
        try{
            return new BaseResponse<>(pageService.deletePage(pageId));
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
