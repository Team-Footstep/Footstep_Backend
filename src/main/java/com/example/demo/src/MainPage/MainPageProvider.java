package com.example.demo.src.MainPage;

import com.example.demo.config.BaseException;
import com.example.demo.src.MainPage.model.GetFollowingNewRes;
import com.example.demo.src.MainPage.model.GetTrendingFootprintsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class MainPageProvider {

    private final MainPageDao mainPageDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainPageProvider(MainPageDao mainPageDao) {
        this.mainPageDao = mainPageDao;
    }

    /*
     * Trending This Week - 누적 stamp 횟수가 높은 순서대로 12개
     * */
    public List<GetTrendingFootprintsRes> retrieveTrendingFootsteps() throws BaseException {
        try{
            return mainPageDao.getTrendingFootsteps();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 팔로우한 사람들의 최신 기록 5개
     * */
    public List<GetFollowingNewRes> retrieveFollowingNew(int userId) throws BaseException {
        if(checkUserExist(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            return mainPageDao.getFollowingNew(userId);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
     * 사용자가 존재하는지 체크
     * */
    public int checkUserExist(int userId) throws BaseException{
        try{
            return mainPageDao.checkUserExist(userId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}