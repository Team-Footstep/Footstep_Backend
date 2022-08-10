package com.example.demo.src.Follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowReq {
    private int userId; // 유저의 Id
    private int followedId; // 팔로우된 사람의 Id
}
