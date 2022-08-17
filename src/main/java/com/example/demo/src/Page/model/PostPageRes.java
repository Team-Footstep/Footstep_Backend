package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostPageRes {
    // 페이지 생성할 때에 반환해줘야 할 값들
    private int pageId; // 페이지 아이디
    private Timestamp createdAt; // 생성된 시각
    private int status; // 페이지의 상태
}
