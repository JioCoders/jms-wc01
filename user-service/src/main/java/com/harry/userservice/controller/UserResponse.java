package com.harry.userservice.controller;

import com.harry.userservice.entity.UserX;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int code;
    private String message;
    private UserX userX;

}
