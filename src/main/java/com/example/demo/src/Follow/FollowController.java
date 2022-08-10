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

    // [Get] 팔로잉 데이터 조회 -> 갯수 세기
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

    // [Patch] 언팔 -> 팔로우 상태만 변경
    @ResponseBody
    @PatchMapping("/unfollow")
    public BaseResponse<DeleteFollowRes> deleteFollow(@RequestParam FollowReq deleteFollowReq){
        try{
            DeleteFollowRes deleteFollowRes = followService.deleteFollow(deleteFollowReq);
            return new BaseResponse<>(deleteFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //  팔로우하는 경우 -> status를 바꿔주기
    @ResponseBody
    @PostMapping("/follow")
    public BaseResponse<PostFollowRes> updateFollow(@RequestParam FollowReq updateFollowReq) throws BaseException{
        try {
            if(followService.checkExist(updateFollowReq)){ // 존재하면 수정
               PostFollowRes postFollowRes = followService.modifyFollow(updateFollowReq);
                return new BaseResponse<>(postFollowRes);
            }else{ // 없었으면 생성
                PostFollowRes createFollowRes = followService.createFollow(updateFollowReq);
                return new BaseResponse<>(createFollowRes);
            }
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
