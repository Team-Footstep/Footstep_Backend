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

    public List<GetTrendingFootprintsRes> retrieveTrendingFootsteps() throws BaseException {
        try{
            return mainPageDao.getTrendingFootsteps();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetFollowingNewRes> sretrieveFollowingNew(int userId) throws BaseException {
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

    public int checkUserExist(int userId) throws BaseException{
        try{
            return mainPageDao.checkUserExist(userId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}