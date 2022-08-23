package com.example.demo.src.Login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLogoutRes {
    private String email;
    private int status;
}
