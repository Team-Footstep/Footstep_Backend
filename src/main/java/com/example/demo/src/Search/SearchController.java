package com.example.demo.src.Search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Search.model.GetSearchRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchProvider searchProvider;

    @Autowired
    public SearchController(SearchProvider searchProvider){
        this.searchProvider = searchProvider;
    }

    /**
     * 유저 이름 검색 api
     * [GET] /search/?word=?&page=?
     * @param word : 검색 된 이름
     * @param page : 페이지 이름
     * @return BaseResponse <GetSearchRes>
     * @author 문현우
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse <GetSearchRes> getSearchRes (@RequestParam("word")String word,@RequestParam("page")int page){
        try{
            if(word.length()==0){
                return new BaseResponse<>(BaseResponseStatus.GET_SEARCH_EMPTY_WORD);
            }
            GetSearchRes getSearchInfoRes = searchProvider.retrieveSearchInfo(word,page);
            return new BaseResponse<>(getSearchInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}