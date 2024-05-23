package com.harry.userservice.service.api;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.harry.userservice.VO.ResponseTemplateVO;
import com.harry.userservice.controller.UserResponse;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.model.req.ReqLogin;
import com.harry.userservice.model.res.ResLogin;

public interface UserService {
    ResponseEntity<ResLogin> loginUser(ReqLogin request);

    UserResponse saveUser(UserX userX);
    UserResponse findByUserId(int userId);
    ResponseTemplateVO getUserWithDepartment(int userId);

    List<UserX> findAll();
}
