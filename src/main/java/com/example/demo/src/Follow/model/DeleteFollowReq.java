package com.example.demo.src.Follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteFollowReq {
    private int unfollowId; // 언팔한 유저 ID
    private int unfollowedId; // 언팔 당한 유저 Id
}
