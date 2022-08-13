package com.example.demo.src.Comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCommentRes {
    private int pageId;
    private int blockId;
    private String comment;
    private int status;
}
