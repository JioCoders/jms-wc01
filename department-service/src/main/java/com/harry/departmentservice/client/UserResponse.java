package com.harry.departmentservice.client;

import com.harry.departmentservice.entity.Department;

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
    private Department department;

}
