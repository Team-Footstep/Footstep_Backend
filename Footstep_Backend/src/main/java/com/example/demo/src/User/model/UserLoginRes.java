package com.example.demo.src.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginRes {
    private BigInteger userId;
    private String email;
    private String userName;
}
