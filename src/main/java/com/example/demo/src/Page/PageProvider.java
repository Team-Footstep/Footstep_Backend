package com.example.demo.src.Page;

import com.example.demo.config.BaseException;
import com.example.demo.src.Page.model.PostPageReq;
import com.example.demo.src.Page.model.PostPageRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PageProvider {
    private final PageDao pageDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PageProvider(PageDao pageDao){
        this.pageDao = pageDao;
    }

    public PostPageRes retrieveBookmarks(PostPageReq postPageReq) throws BaseException {
        try{
            return pageDao.createPage(postPageReq);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
