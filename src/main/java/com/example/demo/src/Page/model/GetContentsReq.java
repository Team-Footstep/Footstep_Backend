package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsReq {
    private int childPageId;
    private String content;
    private int orderNum;
    private int status;
}
