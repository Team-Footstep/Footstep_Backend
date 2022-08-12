package com.example.demo.src.Bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.src.Bookmark.model.GetBookmarksRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class BookmarkProvider {

    private final BookmarkDao bookmarkDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public BookmarkProvider(BookmarkDao bookmarkDao) {
        this.bookmarkDao = bookmarkDao;
    }

    /*
     * 사용자의 북마크한 리스트 반환
     * */
    public List<GetBookmarksRes> retrieveBookmarks(int userId) throws BaseException {
        if(checkUserExist(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            return bookmarkDao.getBookmarks(userId);
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
            return bookmarkDao.checkUserExist(userId);
        } catch (Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}