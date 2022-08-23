package com.example.demo.src.Page.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PutPageRes {
    private int pageId; // 저장한 페이지 아이디
    private Timestamp updatedAt; // 업데이트 시각
    private String message; // 수정 완료 메세지
}
