package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    NOT_LOGIN(false, 2004, "로그인되지 않았습니다."),
    EMPTY_IDX(false, 2005, "userId를 찾을수 없습니다."),
    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users/signup
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_USERNAME(false, 2018, "이름을 입력해주세요. "),
    GET_USERS_EXPIRED_TOKEN(false,2019 ,"토큰이 만료되었습니다." ),
    MODIFY_FAIL_EMAIL(false, 2020, "이메일 변경에 실패하였습니다."),
    PATCH_SAME_EMAIL(false, 2021, "변경하려는 이메일이 기존 이메일과 같습니다."),
    GET_PATCH_EMAIL(false, 2022, "이메일 검증이 실패하였습니다."),
    NOT_EXIST_EMAIL(false, 2023, "해당하는 이메일이 없습니다."),
    GET_FOOT_EMPTY(false,2024 ,"팔로우하는 풋스텝이 없습니다" ),
    GET_FOLLOW_EMPTY(false, 2025, "팔로우하는 페이지가 없습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    FAILED_TO_LOGIN(false,3014,"없는 이메일입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요
    /**
     * 9000 : Comment 오류
     */
    NOT_EXIST_COMMENTID(false, 9000, "해당하는 댓글이 없습니다."),
    NOT_EXIST_COMMENT(false, 9001, "댓글을 입력해주세요."),
    SAME_COMMENT(false, 9002, "댓글 내용이 수정되지 않았습니다."),
    NOT_EXIST_PAGGID_BLOCKID(false, 9003, "페이지 아이디 또는 블록 아이디가 일치하지 않습니다."),
    NOT_EXIST_GET_COMMENT(false, 9004, "조회할 댓글이 없습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
