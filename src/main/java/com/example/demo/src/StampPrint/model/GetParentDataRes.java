package com.example.demo.src.StampPrint.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetParentDataRes {
    private int curPageId;
    private int userId;
    private int depth;

}
