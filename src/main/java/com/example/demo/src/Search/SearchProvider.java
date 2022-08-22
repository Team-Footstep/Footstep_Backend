package com.example.demo.src.Search;

import com.example.demo.config.BaseException;
import com.example.demo.src.Search.model.GetSearchRes;
import com.example.demo.src.Search.model.GetPostsInfoRes;
import com.example.demo.src.Search.model.GetUserInfoRes;
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

    @Autowired
    public SearchProvider(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    /**
     * 검색된 유저,글 정보 반환 api
     * @param word
     * @param page
     * @return GetSearchRes
     * @throws BaseException
     * @author nnlnuu
     */
    public GetSearchRes retrieveSearchInfo(String word,int page) throws BaseException {
        try{
            String userInfoMessage,postInfoMessage;

            List<GetUserInfoRes> userInfoList =  searchDao.getUserInfoByWord(word,page);
            if(userInfoList.isEmpty()){
                userInfoMessage = "검색된 유저 결과가 없습니다.";
            }else{
                userInfoMessage ="검색 유저 결과를 확인하세요";
            }

            List<GetPostsInfoRes> postInfoList = searchDao.getPostInfo(word,page);
            if(postInfoList.isEmpty()){
                postInfoMessage ="검색된 글 결과가 없습니다.";
            }else{
                postInfoMessage ="검색된 글 결과를 확인하세요";
            }
            return new GetSearchRes(userInfoMessage,userInfoList,postInfoMessage,postInfoList);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}