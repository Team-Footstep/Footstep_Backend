package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPageReq {
    private int pageId; // 수정하고자 하는 페이지 아이디
    private int userId;
    private String preview;
    private int status; // page 의 상태
    private String stampOrPrint; // stamp 인지 print 인지
    private int bookmark;
    private int access;
    private List<GetContentsRes> contentList;
    private int depth;

}
