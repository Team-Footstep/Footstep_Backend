package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

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

    /**
     * 페이지 생성 api
     * @param postPageReq
     * @return PostPageRes
     * @throws BaseException
     */

    public PostPageRes createPage(PostPageReq postPageReq) throws BaseException{
        try{
            return pageDao.createPage(postPageReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 페이지 공개/미공개 설정
     * @param patchAccessReq
     * @author ro-el
     * @return void
     * */
    public void updateAccess(PatchAccessReq patchAccessReq) throws BaseException {
        try {
            pageDao.updateAccess(patchAccessReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 페이지 저장 api
     * @param patchPageReq
     * @return PatchPageRes
     * @throws BaseException
     * @author nnlnuu
     */
    public PutPageRes updatePage(PutPageReq patchPageReq) throws BaseException {
        try {
            return pageDao.updatePage(patchPageReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    /**
     * 페이지 북마크 설정/해제
     * @param patchBookmarkReq
     * @return void
     * @author ro-el
     * */
    public void updateBookmark(PatchBookmarkReq patchBookmarkReq) throws BaseException {
        try {
            pageDao.updateBookmark(patchBookmarkReq);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /*
     * 하위 페이지 삭제
     * */
    public Boolean deletePage(int pageId) throws BaseException {
        if (checkDeleteTopPage(pageId) == 1){ // 최상단 페이지 삭제 불가
            throw new BaseException(PAGES_CANNOT_DELETE_TOP_PAGE);
        }
        try {
            pageDao.deletePage(pageId);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 삭제하려는 페이지가 최상단 페이지인 경우 validation
     * */
    public int checkDeleteTopPage(int pageId) throws BaseException {
        try {
            return pageDao.checkDeleteTopPage(pageId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkExist(int parentPageId) {
        return pageDao.checkExist(parentPageId);
    }


    /**
     * 깊이 제한 확인 메소드
     * @param pageId
     * @return boolean
     * @author nnlnuu
     */
    public boolean checkDepth(int pageId){
        return pageDao.checkDepth(pageId);
    }
}
