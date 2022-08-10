package com.example.demo.src.Block;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Block.model.PostPageReq;
import com.example.demo.src.Block.model.PostPageRes;
import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PageService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PageDao pageDao;
    private final PageProvider pageProvider;

    @Autowired
    public PageService(PageDao pageDao, PageProvider pageProvider){
        this.pageDao = pageDao;
        this.pageProvider = pageProvider;
    }

    public PostPageRes createPage(PostPageReq postPageReq) throws BaseException{
        try{
           PostPageRes postPageRes =  pageDao.createPage(postPageReq);
           return new PostPageRes(postPageRes.getUserId(),postPageRes.getPageId(),
                   postPageRes.getCreatedAt(),postPageRes.getStatus());
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
