package com.example.demo.src.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String email;
    private String job;
    private String userName;
    private String introduction;
    private String userImgUrl;
    private int status;
    private String token;
}
