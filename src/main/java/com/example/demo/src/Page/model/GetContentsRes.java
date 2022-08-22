package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsRes {
    private int userId;
    private int blockId;
    private int curPageId;
    private int childPageId;
    private String content;
    private int isNewBlock; // 1 : 새 블럭, 0 : 기존 블럭
    //private int orderNum;
    private int status;
}
