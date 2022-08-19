package com.example.demo.src.Follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Follow.model.*;
import com.example.demo.utils.JwtService;
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
    private final JwtService jwtService;

    @Autowired
    public FollowController(FollowService followService,FollowProvider followProvider,JwtService jwtService){
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    /**
     * 팔로우 수 조회 api
     * [Get] /follows/get/{userId}
     * @return BaseResponse<GetFollowInfoRes>
     * @author nnlnuu
     * */
    @ResponseBody
    @GetMapping("/get/{userId}")
    public BaseResponse<GetFollowInfoRes> getFollowInfo(@PathVariable int userId){
        try{
            GetFollowInfoRes getFollowInfoRes = followProvider.retrieveFollow(userId);
            return new BaseResponse<>(getFollowInfoRes);
        } catch (BaseException exception) {
           return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 언팔 api
     * [Delete] /follows/unfollow
     * @return BaseResponse<DeleteFollowRes>
     * @author nnlnuu
     */
    @ResponseBody
    @DeleteMapping("/unfollow")
    public BaseResponse<DeleteFollowRes> deleteFollow(@RequestBody FollowReq deleteFollowReq)throws BaseException{
        try{
            DeleteFollowRes deleteFollowRes = followService.deleteFollow(deleteFollowReq);
            return new BaseResponse<>(deleteFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 팔로우 api
     *@apiNote [Post] /follows/follow
     * @return BaseResponse<PostFollowRes>
     * @author nnlnuu
     */
    @ResponseBody
    @PostMapping("/follow")
    public BaseResponse<PostFollowRes> createFollow(@RequestBody FollowReq updateFollowReq) throws BaseException{
        try {
                PostFollowRes createFollowRes = followService.createFollow(updateFollowReq);
                return new BaseResponse<>(createFollowRes);
        }
        catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
