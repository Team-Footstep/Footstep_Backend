package com.example.demo.src.User.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProfileRes {
    private int userId;
    private GetStampTopPageRes getStampTopPageRes;
    private GetPrintTopPageRes getPrintTopPageRes;
    private String userImgUrl;
    private String userName;
    private String job;
    private String introduction;
    private int stampNum;
    private int footprintNum;

}