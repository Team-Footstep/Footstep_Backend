package com.example.demo.src.Follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
public class PostFollowRes {
    private Timestamp updatedAt; // 생성,업데이트 시각
    private int status; // 상태
    private int follower; // 팔로우 한 사람
    private int followee; // 팔로우 당한 사람
}
