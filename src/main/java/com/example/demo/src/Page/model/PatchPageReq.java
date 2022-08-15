package com.example.demo.src.Page.model;

import com.sun.tools.javac.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchPageReq {
    private String preview;
    private int status; // page의 상태
    private int stampOrNot; // stamp 인지 아닌지
    private int bookmark;
    private int access;
    private List<GetContentsReq> contentList;

}
