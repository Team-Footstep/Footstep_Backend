package com.example.demo.src.Search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostsInfoRes {

    //todo : 주석 작성하기
    private GetUserInfoRes userInfoList; // 유저 정보

    private String preview; // 프리뷰 글
    private int parentBlockId; // 자식 페이지의 아이디
    private Timestamp updatedAt; // 업데이트 시각
    private List<GetContentsRes> contentsList; // 컨텐츠 리스트

    private int commentNum; // comment 갯수
    private int stampNum;//stamp 갯수
    private int footPrintNum; // footPrint 갯수

}