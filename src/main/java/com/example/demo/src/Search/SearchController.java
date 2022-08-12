package com.example.demo.src.Search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Search.model.GetSearchReq;
import com.example.demo.src.Search.model.GetSearchRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchProvider searchProvider;
    private final JwtService jwtService;

    @Autowired
    public SearchController(SearchProvider searchProvider,JwtService jwtService){
        this.searchProvider = searchProvider;
        this.jwtService = jwtService;
    }

    /**
     * 유저 이름 검색 기능
     * @param getSearchReq : 검색 된 이름
     * @return 유저 정보 반환
     * @author 문현우
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse <GetSearchRes> getSearchRes (@RequestBody GetSearchReq getSearchReq ) {
        try{
            //PROCEED : VALIDATION 처리중(이름 길이)
            if(getSearchReq.getWord().length()==0){ //이름 길이
                return new BaseResponse<>(BaseResponseStatus.GET_SEARCH_EMPTY_WORD);
            }
            GetSearchRes getSearchInfoRes = searchProvider.retrieveSearchInfo(getSearchReq);
            return new BaseResponse<>(getSearchInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}