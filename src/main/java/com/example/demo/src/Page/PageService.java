package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Page.model.PatchAccessReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

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

}