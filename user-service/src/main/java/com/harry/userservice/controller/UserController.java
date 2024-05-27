package com.harry.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harry.userservice.VO.Department;
import com.harry.userservice.VO.ResponseTemplateVO;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.model.req.ReqLogin;
import com.harry.userservice.model.res.ResLogin;
import com.harry.userservice.service.api.UserService;
import com.harry.userservice.swagger.UserInfo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController implements UserInfo {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResLogin> loginUser(@RequestBody ReqLogin request) {
        log.info("UserController => loginUser()=>" + request.getEmail());
        return userService.loginUser(request);
    }

    @PostMapping("/save")
    public UserResponse saveUser(@RequestBody UserX userX) {
        log.info("UserController ========> saveUser()" + userX.toString());
        return userService.saveUser(userX);
    }

    @GetMapping("/findAll")
    public List<UserX> findAll() {
        log.info("UserController =======> findAll()");
        return userService.findAll();
    }

    @GetMapping("/findByUserId/{id}")
    public UserResponse findByUserId(@PathVariable("id") int userId) {
        log.info("UserController =======> findByUserId()" + userId);
        return userService.findByUserId(userId);
    }

    int retryCount = 1;

    @GetMapping("/vo/{id}")
    @CircuitBreaker(name = "userDeptBreaker", fallbackMethod = "userDeptFallback")
    // @Retry(name = "userDeptRetry", fallbackMethod = "userDeptFallback")
    public ResponseEntity<ResponseTemplateVO> getUserWithDepartment(@PathVariable("id") int userId) {
        log.info("UserController =======> getUserWithDepartment()" + userId);
        log.info("retryCount=====> {}", retryCount);
        retryCount++;
        if (retryCount > 3) {
            retryCount = 1;
        }
        ResponseTemplateVO vo = userService.getUserWithDepartment(userId);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    // creatting fallback method for Circuit-Breaker and retry
    public ResponseEntity<ResponseTemplateVO> userDeptFallback(int userId, Exception ex) {
        log.info("Fallback is executed because dept service is down!", ex.getMessage());
        UserResponse userX = userService.findByUserId(userId);
        Department dept = new Department();
        dept.setDepartmentId(1101L);
        dept.setDepartmentName("setDepartmentName-dummy");
        dept.setDepartmentCode("setDepartmentCode--dummy");
        dept.setDepartmentAddress("setDepartmentAddress--dummy fallback");

        ResponseTemplateVO vo = new ResponseTemplateVO();
        vo.setStatus(1);
        vo.setMessage("success");
        vo.setDepartment(dept);
        vo.setUserX(userX.getUserX());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

}
