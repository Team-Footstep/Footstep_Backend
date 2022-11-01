package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PostPageReq {
    private int parentPageId; // 부모 페이지 아이디
    private int parentBlockId; // 부모 블럭 아이디
    private int userId;
    private String stampOrPrint; // stamp 인지 아닌지
}
