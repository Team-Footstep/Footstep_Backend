package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return BaseResponse<PostCommentRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/{pageId}/{blockId}") // (POST) 127.0.0.1:8080/comment/{pageId}/{blockId}
    public BaseResponse<PostCommentRes> createCommment(@PathVariable int pageId, @PathVariable int blockId, @RequestBody PostCommentReq postCommentReq) throws BaseException {

        if (postCommentReq.getContent() == null) {
            return new BaseResponse<>(NOT_EXIST_COMMENT);
        }

        PostCommentRes postCommentRes = commentService.createComment(pageId, blockId, postCommentReq);
        return new BaseResponse<>(postCommentRes);

    }
    /**
     * 댓글수정 API
     * [PATCH] /comment/{pageId}/{blockId}
     * @return BaseResponse<PatchCommentRes>
     */
    // Body
    @ResponseBody
    @PatchMapping("/{pageId}/{blockId}") // (POST) 127.0.0.1:8080/comment/{pageId}/{blockId}
    public BaseResponse<PatchCommentRes> modifyComment(@PathVariable int pageId, @PathVariable int blockId, @RequestBody PatchCommentReq patchCommentReq) throws BaseException {
        //수정할 댓글 commentId가 존재하는지
        if(commentService.checkCommentExist(patchCommentReq.getCommentId())==0){
            System.out.println("수정할 댓글 아이디가 존재하지 않음");
            return new BaseResponse<>(NOT_EXIST_COMMENTID);
        }
        PatchCommentRes patchCommentRes = commentService.modifyComment(pageId, blockId, patchCommentReq);
        return new BaseResponse<>(patchCommentRes);

    }
    /**
     * 댓글삭제 API
     * [PATCH] /delete/{commentId}
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PatchMapping("/delete/{commentId}") // (POST) 127.0.0.1:8080/comment/delete/{pageId}/{blockId}/{commentId}
    public BaseResponse<DeleteCommentRes> modifyCommment(@RequestBody DeleteCommentReq deleteCommentReq, @PathVariable int commentId) throws BaseException {
        if(commentService.checkCommentExist(commentId) == 0){
            return new BaseResponse<>(NOT_EXIST_COMMENTID);
        }
        if(commentService.checkCommentStatus(commentId)==0){
            return new BaseResponse<>(NOT_EXIST_COMMENTID);
        }
        DeleteCommentRes deleteCommentRes = commentService.deleteComment(deleteCommentReq, commentId);
        //result = 1 : 성공 , 0: 실
        return new BaseResponse<>(deleteCommentRes);

    }
    /**
     * 댓글가져오기 API
     * [GET] /{pageId}/{blockId}
     */
    // Body
    @ResponseBody
    @GetMapping("/{pageId}/{blockId}") // (POST) 127.0.0.1:8080/comment/delete/{pageId}/{blockId}/{commentId}
    public BaseResponse<List<GetCommentRes>> getComment(@PathVariable int pageId, @PathVariable int blockId) throws BaseException {
        //해당 PageId/blockId에 댓글이 존재하는지
        if(commentService.checkCommentInPageBlock(pageId, blockId) == 0){
            return new BaseResponse<>(NOT_EXIST_PAGGID_BLOCKID);
        }
        //댓글 내용이 null 이라면
        if(commentService.commentContent(pageId, blockId)==1){
            return new BaseResponse<>(NOT_EXIST_GET_COMMENT);
        }
        try{
            List<GetCommentRes> getCommentRes = commentProvider.getComment(pageId, blockId);
            return new BaseResponse<>(getCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
