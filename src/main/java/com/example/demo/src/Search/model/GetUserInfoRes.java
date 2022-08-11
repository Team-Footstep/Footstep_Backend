package com.example.demo.src.Search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {

    private int userIdx; // 유저 아이디
    private String userName; //유저 이름
    private String introduction; // 유저 소개
    private String job; // 직업
    private String userImgUrl; // 유저 이미지 링크
    private int pageId; // 페이지Id
}