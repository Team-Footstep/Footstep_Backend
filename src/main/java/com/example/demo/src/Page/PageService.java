package com.example.demo.src.Page;

import com.example.demo.config.BaseException;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Page.model.PatchPageReq;
import com.example.demo.src.Page.model.PatchPageRes;
import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.Page.model.PostPageRes;
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
                return pageDao.createPage(postPageReq);
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    public PatchPageRes updatePage(PatchPageReq patchPageReq) throws  BaseException{
        try{
            return pageDao.updatePage(patchPageReq);
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public boolean checkExist(int parentPageId) {
        return pageDao.checkExist(parentPageId);
    }
}
