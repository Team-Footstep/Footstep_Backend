package com.example.demo.src.Search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchReq {
    private String word; // 검색어
    private int page; // 몇 페이지 인지
}
