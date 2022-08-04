package com.example.demo.src.MainPage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewFootprintsRes {
    private int userId;
    private int blockId;
    private String createdAt;
    private String preview;
    private List<String> content;
    private int stampNum;
    private int footprintNum;
    private int commentNum;
}
