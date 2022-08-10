package com.example.demo.src.Follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Follow.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@RestController
@RequestMapping("/follows")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowProvider followProvider;
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService,FollowProvider followProvider){
        this.followProvider = followProvider;
        this.followService = followService;
    }

    // 팔로잉 데이터 조회 -> 갯수 세기
    @ResponseBody
    @GetMapping("/get")
    public BaseResponse<GetFollowInfoRes> getFollowInfo(@RequestParam int userId){
        try{
            GetFollowInfoRes getFollowInfoRes = followProvider.retrieveFollow(userId);
            return new BaseResponse<>(getFollowInfoRes);
        } catch (BaseException exception) {
           return new BaseResponse<>(exception.getStatus());
        }
    }
    @ResponseBody
    @PatchMapping("/unfollow")
    public BaseResponse<DeleteFollowRes> deleteFollow(@RequestParam PostFollowReq postFollowReq){
        try{
            DeleteFollowRes deleteFollowRes = followService.deleteFollow(postFollowReq);
            return new BaseResponse<>(deleteFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //  언팔 하는 경우 -> status를 바꿔주기
    @ResponseBody
    @PostMapping("/follow")
    public BaseResponse<PostFollowRes> createFollow(@RequestParam PostFollowReq postFollowReq) throws BaseException{
        try {
            PostFollowRes postFollowRes = followService.createFollow(postFollowReq);
            return new BaseResponse<>(postFollowRes);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
