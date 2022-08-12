package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.Comment.model.PostCommentRes;
import com.example.demo.src.User.UserProvider;
import com.example.demo.src.User.UserService;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/comment") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class CommentController {
    @Autowired
    private CommentProvider commentProvider;
    @Autowired
    private CommentService commentService;
    public CommentController(CommentProvider commentProvider, CommentService commentService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
    }
    /**
     * 댓글작성 API
     * [POST] /comment/{pageId}/{blockId}
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/{pageId}/{blockId}") // (POST) 127.0.0.1:8080/comment/userId //userId : 댓글 작성자
    public BaseResponse<PostCommentRes> createCommment(@PathVariable int pageId, @PathVariable int blockId, @RequestBody PostCommentReq postCommentReq) {
        PostCommentRes postCommentRes = commentService.createComment(pageId, blockId, postCommentReq);
        return new BaseResponse<>(postCommentRes);

    }

}
