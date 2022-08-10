package com.example.demo.src.Follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFollowReq {
    //팔로우를 했을 때
    private int userId; // 본인 Id
    private int followedId; // 팔로우 당한 유저 Id;
}
