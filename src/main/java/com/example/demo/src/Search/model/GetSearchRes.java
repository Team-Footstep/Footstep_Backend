package com.example.demo.src.Search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetSearchRes {

    String userInfoMessage; // 유저 검색 정보 메세지
    List<GetUserInfoRes> usersInfoList; // 유저 검색 정보 리스트
    String postInfoMessage; // 글 검색 정보 메세지
    List<GetPostsInfoRes> postInfoList; // 글 검색 정보 리스트
}