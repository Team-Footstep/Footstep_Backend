package com.example.demo.src.Comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PatchCommentRes {
    private int userId;
    private int pageId;
    private int blockId;
    private String content;
    private LocalDateTime timestamp;
}
