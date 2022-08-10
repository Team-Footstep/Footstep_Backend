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
    private Timestamp createdAt;
    private int status;
    private int followee;
    private int follower;
}
