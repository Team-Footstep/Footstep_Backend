package com.example.demo.src.Comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCommentReq {
    private int pageId;
    private int blockId;
}
