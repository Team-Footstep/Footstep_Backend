package com.example.demo.src.Comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentRes {
    private int userId;
    private int pageId;
    private int blockId;
    private String content;
    private int status;
    private LocalDateTime timestamp;

}