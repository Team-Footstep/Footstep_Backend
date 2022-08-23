package com.example.demo.src.Login.model;

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
