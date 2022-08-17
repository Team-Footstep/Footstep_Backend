package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPageRes {// 요청한 페이지에 대한 응답
    private int pageId;
    private String preview;
    private int status;
    private int stampOrPrint;
    private int bookmark;
    private int access;
    private List<GetContentsRes> contents;
}
