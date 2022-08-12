package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
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
    private final PageProvider pageProvider;

    @Autowired
    public PageService(PageDao pageDao, PageProvider pageProvider) {
        this.pageDao = pageDao;
        this.pageProvider = pageProvider;
    }

    public PostPageRes createPage(PostPageReq postPageReq) throws BaseException{
        try{
            return pageDao.createPage(postPageReq);
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
