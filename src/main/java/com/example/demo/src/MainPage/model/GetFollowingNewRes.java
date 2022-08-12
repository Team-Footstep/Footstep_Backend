package com.example.demo.src.MainPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowingNewRes {
    private int userId;
    private int pageId;
    private int parentBlockId;
    private String preview;
    private List<GetContentsRes> contents;
    private String createdAt;
    private int stampNum;
    private int footprintNum;
    private int commentNum;
}
