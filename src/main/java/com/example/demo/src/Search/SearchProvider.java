package com.example.demo.src.Search;

import com.example.demo.config.BaseException;
import com.example.demo.src.Search.model.GetSearchRes;
import com.example.demo.src.Search.model.GetPostsInfoRes;
import com.example.demo.src.Search.model.GetUserInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SearchProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final JwtService jwtService;

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }

    public GetSearchRes retrieveSearchInfo(String word) throws BaseException {
        // TODO: 형식적 VALIDATION 처리

        try{
            List<GetPostsInfoRes> postInfoList = searchDao.getPostInfo(word);
            List<GetUserInfoRes> userInfoList =  searchDao.getUserInfoByWord(word);
            return new GetSearchRes(userInfoList,postInfoList);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}