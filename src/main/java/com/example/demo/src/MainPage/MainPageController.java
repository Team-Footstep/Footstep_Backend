package com.example.demo.src.MainPage;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.MainPage.model.GetFollowingNewRes;
import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mainpage") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class MainPageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MainPageProvider mainPageProvider;
    @Autowired
    private final MainPageService mainPageService;


    public MainPageController(MainPageProvider mainPageProvider, MainPageService mainPageService){
        this.mainPageProvider = mainPageProvider;
        this.mainPageService = mainPageService;
    }

    @ResponseBody
    @GetMapping("/trending")
    public BaseResponse<List<GetTrendingFootprintsRes>> getNewFootsteps() {
        try{
            List<GetTrendingFootprintsRes> getTrendingFootprintsRes = mainPageProvider.retrieveTrendingFootsteps();
            return new BaseResponse<>(getTrendingFootprintsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/new/{userId}")
    public BaseResponse<List<GetFollowingNewRes>> getNewFootsteps(@PathVariable int userId) {
        try{
            List<GetFollowingNewRes> getFollowingNewRes = mainPageProvider.retrieveFollowingNew(userId);
            return new BaseResponse<>(getFollowingNewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}