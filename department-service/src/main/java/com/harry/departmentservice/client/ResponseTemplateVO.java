package com.harry.departmentservice.client;

import java.util.List;
import java.util.ArrayList;
import com.harry.departmentservice.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplateVO {
    private int status;
    private String message;
    private List<Department> departmentList = new ArrayList<>();
    private List<UserX> userList = new ArrayList<>();
}
