package com.example.demo.src.Follow.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class DeleteFollowRes {
    private int unfollowId;
    private int unfollowedId;
}
