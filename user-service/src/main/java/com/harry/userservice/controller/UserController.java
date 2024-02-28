package com.harry.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.harry.userservice.VO.ResponseTemplateVO;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.service.UserService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public UserResponse saveUser(@RequestBody UserX userX) {
        log.info("UserController ========> saveUser()" + userX.toString());
        return userService.saveUser(userX);
    }

    @GetMapping("/findAll")
    public List<UserX> findAll() {
        log.info("UserController =======> findAll()" );
        return userService.findAll();
    }

    @GetMapping("/findByUserId/{id}")
    public UserResponse findByUserId(@PathVariable("id") Long userIdLong) {
        log.info("UserController =======> findByUserId()" + userIdLong);
        return userService.findByUserId(userIdLong);
    }

    @GetMapping("/vo/{id}")
    public ResponseTemplateVO getUserWithDepartment(@PathVariable("id") Long userIdLong) {
        log.info("UserController =======> getUserWithDepartment()" + userIdLong);
        return userService.getUserWithDepartment(userIdLong);
    }
    
}
