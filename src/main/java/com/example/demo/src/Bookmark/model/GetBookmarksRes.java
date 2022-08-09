package com.example.demo.src.Bookmark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarksRes {
    private int userId; // 북마크 누른 사람
    private int pageId;
    private String preview; // 바로가기 제목
    private String stampOrPrint; // stamp/print 상태 표기
}
