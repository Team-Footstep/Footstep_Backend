package com.example.demo.src.StampPrint.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostStampReq {
    private int loginUserId; // stamp 한 유저Id
    private int blockId; // stamp 당할 블록 Id
    private int followeeId; // stamp 당한 블록 유저Id
}
