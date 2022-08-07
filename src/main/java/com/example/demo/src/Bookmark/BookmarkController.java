package com.example.demo.src.Bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Bookmark.model.GetBookmarksRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class BookmarkController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BookmarkProvider bookmarkProvider;
    @Autowired
    private final BookmarkService bookmarkService;


    public BookmarkController(BookmarkProvider bookmarkProvider, BookmarkService bookmarkService){
        this.bookmarkProvider = bookmarkProvider;
        this.bookmarkService = bookmarkService;
    }

    /*
     * 북마크
     * 입력 받은 유저가 북마크한 기록 내용 반환
     * */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetBookmarksRes>> getBookmarks(@PathVariable int userId) {
        try{
            List<GetBookmarksRes> getBookmarksRes = bookmarkProvider.retrieveBookmarks(userId);
            return new BaseResponse<>(getBookmarksRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}