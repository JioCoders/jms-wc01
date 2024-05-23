package com.harry.departmentservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harry.departmentservice.client.ResponseTemplateVO;
import com.harry.departmentservice.client.UserClient;
import com.harry.departmentservice.client.UserResponse;
import com.harry.departmentservice.client.UserX;
import com.harry.departmentservice.entity.Department;
import com.harry.departmentservice.service.DepartmentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/dept")
@Slf4j
public class DepartmentController {

    @Autowired
    public UserClient userClient;

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/save")
    public Department saveDepartment(@RequestBody Department department) {
        log.info("DepartmentController[] => saveDepartment(department)");
        return departmentService.saveDepartment(department);
    }

    @GetMapping("/findAll")
    public List<Department> findAll() {
        log.info("DepartmentController[] => findAll(-)");
        return departmentService.findAll();
    }

    /**
     * findDepartmentById
     */
    @GetMapping("/find/{id}")
    public Department findByDepartmentId(@PathVariable("id") long departmentId) {
        log.info("DepartmentController[] => findByDepartmentId(id)");
        return departmentService.findByDepartmentId(departmentId);
    }

    @GetMapping("/findOneWithUser/{id}")
    public UserResponse findOneWithUser(@PathVariable("id") Long userId) {
        log.info("DepartmentController[] => findOne-withUser()" + userId);
        UserResponse userResponse = userClient.findByUserId(userId);
        log.info(userResponse.toString());
        Department dept = findByDepartmentId(userId);
        userResponse.setDepartment(dept);
        return userResponse;
    }

    @GetMapping("/findAllWithUser")
    public ResponseTemplateVO findAllWithUser() {
        log.info("DepartmentController[] => findAll-withUser(-)");
        List<UserX> users = userClient.findAllUsers();
        List<Department> depts = findAll();
        ResponseTemplateVO vo = new ResponseTemplateVO();
        vo.setStatus(1);
        vo.setMessage("success");
        vo.setUserList(users);
        vo.setDepartmentList(depts);
        return vo;
    }
}
