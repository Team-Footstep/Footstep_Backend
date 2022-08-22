package com.example.demo.src.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLoginRes {
    private String email;
    private String token;
}
