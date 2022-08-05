package com.example.demo.src.MainPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetTrendingFootprintsRes {
    private int userId;
    private int blockId;
    private String content;
    private int footprintNum; // 해당 블럭의 전체 Footprint 횟수
}
