package com.example.demo.src.Follow;

import com.example.demo.config.BaseResponse;
import com.example.demo.src.Follow.model.GetFollowInfoRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follow")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowProvider followProvider;
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService,FollowProvider followProvider){
        this.followProvider = followProvider;
        this.followService = followService;
    }

//    @ResponseBody
//    @GetMapping("/follow")
//    public BaseResponse<GetFollowInfoRes>
}
