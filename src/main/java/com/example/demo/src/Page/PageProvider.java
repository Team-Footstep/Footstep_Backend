package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.src.Page.model.GetPageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class PageProvider {

    private final PageDao pageDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PageProvider(PageDao pageDao) {
        this.pageDao = pageDao;
    }

    /*
     * 페이지 진입 시 내용 가져오기
     * */
    public GetPageRes retrievePage(int pageId) throws BaseException {
        if(checkPageExist(pageId) == 0){
            throw new BaseException(PAGES_NONE_PAGE_ID);
        }
        if(checkPageAccess(pageId) == 0){
            throw new BaseException(PAGES_PAGE_CANNOT_ACCESS);
        }
        try {
            return pageDao.retrievePage(pageId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 없는 페이지인 경우 validation
     * */
    public int checkPageExist(int pageId) throws BaseException{
        try{
            return pageDao.checkPageExist(pageId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 미공개 페이지인 경우 validation
     * */
    public int checkPageAccess(int pageId) throws BaseException{
        try{
            return pageDao.checkPageAccess(pageId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}

