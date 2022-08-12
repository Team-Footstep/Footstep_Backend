package com.example.demo.src.User.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class GetUserRes {
    private String email;
    private String job;
    private String userName;
    private String userImgUrl;
    private String introduction;
}
