package com.example.demo.src.Search.model;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
public class GetContentsRes {
    private int blockId; // 블록 아이디
    private String content; // 블록 별 컨텐츠
    private int orderNum; // block 순서

}
