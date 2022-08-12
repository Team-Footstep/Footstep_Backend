package com.example.demo.src.User.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchEmailReq {
    private int auth;
    private int userId;
    private String email;
}
