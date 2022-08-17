package com.example.demo.src.Follow;


import com.example.demo.config.BaseException;
import com.example.demo.src.Follow.model.DeleteFollowRes;
import com.example.demo.src.Follow.model.FollowReq;
import com.example.demo.src.Follow.model.PostFollowRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;

    @Autowired
    public FollowService(FollowDao followDao){
        this.followDao = followDao;
    }

    //언팔에서 팔로우
    public PostFollowRes createFollow(FollowReq createFollowReq) throws BaseException {
        try {
            return followDao.createFollow(createFollowReq);
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DeleteFollowRes deleteFollow(FollowReq deletedFollowReq) throws BaseException {
        try {
            return followDao.deleteFollow(deletedFollowReq);
        }catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
