package com.example.demo.src.Comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.vm.ci.meta.Local;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentRes {
    private int commentId;
    private int pageId;
    private int blockId;
    private int userId;
    private String userName;
    private String userImgUrl;
    private String content;
    private String updatedAt;
}
