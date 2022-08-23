package com.example.demo.src.StampPrint.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChildPageBlocksRes {
    private int blockId;
    private String content;
    private int orderNum;
}
