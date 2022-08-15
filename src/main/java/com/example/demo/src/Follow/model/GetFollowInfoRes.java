package com.example.demo.src.Follow.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowInfoRes {
    private int follower; // 나를 팔로우 한 사람 수
    private int followee; // 내가 팔로우 한 사람 수
}
