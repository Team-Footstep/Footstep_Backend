package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.src.Page.model.PatchAccessReq;
import com.example.demo.src.Page.model.PatchBookmarkReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    /*
     * 페이지 공개/미공개 설정
     * */
    public void updateAccess(PatchAccessReq patchAccessReq) throws BaseException {
        try{
            pageDao.updateAccess(patchAccessReq);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 페이지 북마크 설정/해제
     * */
    public void updateBookmark(PatchBookmarkReq patchBookmarkReq) throws BaseException {
        try{
            pageDao.updateBookmark(patchBookmarkReq);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}