package com.example.demo.src.Follow.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteFollowRes {
    private int userId;
    private int followedId;
    private String message;
}
