package com.harry.userservice.VO;

import com.harry.userservice.entity.UserX;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {
    private int status;
    private String message;
    private Department department;
    private UserX userX;
}
