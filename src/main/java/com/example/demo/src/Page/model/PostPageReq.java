package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PostPageReq {

    //저장하라고 넘어올 정보들 정리하기
    private int parentPageId; // 부모 페이지 아이디
    private int parentBlockId; // 부모 블럭 아이디
    private boolean topOrNot; // top 인지 아닌지
    private int access;
    private String stampOrPrint; // stamp 인지 아닌지
    private String preview; // 제목으로 저장할 텍스트
    private int depth; // 깊이
}
