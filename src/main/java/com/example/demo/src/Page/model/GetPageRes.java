package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPageRes {// 요청한 페이지에 대한 응답
    private int pageId;                 // 현재 페이지 아이디
    private int topOrNot;               // 최상단 페이지 여부
    private String preview;             // 페이지 프리뷰 내용 (topOrNot 값이 1이면 null)
    private int parentBlock;          // 부모 블럭 아이디 (경로 표시를 위함)
    private String parentBlockContent;  // 부모 블럭의 내용
    private int access;                 // 공개 여부
    private int bookmark;               // 북마크 여부
    private List<GetBlocksRes> blocks;
}
