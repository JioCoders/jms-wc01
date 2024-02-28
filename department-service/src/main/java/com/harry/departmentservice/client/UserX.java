package com.harry.departmentservice.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserX {
    private Long userIdLong;
    private String firstName;
    private String lastName;
    private String emailId;
    private Long departmentId;
}