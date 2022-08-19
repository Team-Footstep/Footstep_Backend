package com.example.demo.src.Page;

import com.example.demo.config.BaseException;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Page.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class PageService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PageDao pageDao;

    @Autowired
    public PageService(PageDao pageDao) {
        this.pageDao = pageDao;
    }


    public PostPageRes createPage(PostPageReq postPageReq) throws BaseException{
        try{
            if(checkDepth(postPageReq.getParentPageId())) { // 깊이가 15 이상일땐 페이지 생성 X
                throw new BaseException(BaseResponseStatus.OVER_PAGE_ERROR);
            }
            return pageDao.createPage(postPageReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /*
     * 페이지 공개/미공개 설정
     * */
    public void updateAccess(PatchAccessReq patchAccessReq) throws BaseException {
        try {
            pageDao.updateAccess(patchAccessReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public PatchPageRes updatePage(PatchPageReq patchPageReq) throws BaseException {
        try {


            return pageDao.updatePage(patchPageReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    /*
     * 페이지 북마크 설정/해제
     * */
    public void updateBookmark(PatchBookmarkReq patchBookmarkReq) throws BaseException {
        try {
            pageDao.updateBookmark(patchBookmarkReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    public boolean checkExist(int parentPageId) {
        return pageDao.checkExist(parentPageId);
    }

    public boolean checkDepth(int pageId){
        return pageDao.checkDepth(pageId);
    }
}
