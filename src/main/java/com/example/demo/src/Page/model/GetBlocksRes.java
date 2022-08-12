package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBlocksRes {
    private int blockId;
    private String content;
    private int childPageId;
    private int originalId;
    private int followeeId;
    private int status;
    private int stampNum;
    private int footprintNum;
}