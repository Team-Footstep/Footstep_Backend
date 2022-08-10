package com.example.demo.src.Follow;


import com.example.demo.config.BaseException;
import com.example.demo.src.Follow.model.GetFollowInfoRes;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class FollowProvider {

    final private FollowDao followDao;

    public FollowProvider(FollowDao followDao){
        this.followDao = followDao;
    }

    public GetFollowInfoRes retrieveFollow(int userId) throws BaseException{
        try{
            return followDao.getFollow(userId);
        }catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
