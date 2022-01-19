package com.example.restfulwebservice.user;

//HTTP Status code
// 2XX -> OK
// 4XX -> Client 문제
// 5XX -> Server 문제

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //httpStatus code : 400번대로 전달
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
