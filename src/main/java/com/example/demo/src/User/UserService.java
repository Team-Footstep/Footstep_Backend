package com.example.demo.src.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {

    private final UserDao userDao;
    private final UserProvider userProvider;



    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider) {
        this.userDao = userDao;
        this.userProvider = userProvider;
    }
}