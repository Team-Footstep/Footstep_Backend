package com.example.demo.src.Search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchRes {
    //todo : 주석 작성하기
    List<GetUserInfoRes> usersInfoList; // 유저 검색 정보 리스트
    List<GetPostsInfoRes> postInfoList; // 글 검색 정보 리스트
}