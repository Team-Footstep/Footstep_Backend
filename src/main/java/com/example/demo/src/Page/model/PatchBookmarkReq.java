package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchBookmarkReq {
    private int pageId;
    private int bookmark;
}