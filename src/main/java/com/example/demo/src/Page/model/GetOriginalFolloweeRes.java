package com.example.demo.src.Page.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOriginalFolloweeRes {
    private int originalId;
    private String originalImgUrl;
    private int followeeId;
    private String followeeImgUrl;
}