package com.harry.userservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.harry.userservice.VO.Department;
import com.harry.userservice.VO.ResponseTemplateVO;
import com.harry.userservice.controller.UserResponse;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public UserResponse saveUser(UserX userX) {
        log.info("UserService =======> findByUserId()" + userX.toString());
        UserResponse res = new UserResponse();
        UserX userX2;
        try {
            userX2 = userRepository.save(userX);
            res.setCode(1);
            res.setMessage("Success");
            res.setUserX(userX2);
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public UserResponse findByUserId(Long userIdLong) {
        log.info("UserService =======> findByUserId()" + userIdLong);
        UserResponse res = new UserResponse();
        UserX userX2;
        try {
            userX2 = userRepository.findByuserIdLong(userIdLong);
            res.setCode(1);
            res.setMessage("Success");
            res.setUserX(userX2);
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    public ResponseTemplateVO getUserWithDepartment(Long userIdLong) {
        log.info("UserService =======> getUserWithDepartment()=" + userIdLong);
        UserResponse userX = findByUserId(userIdLong);
        Department dept = restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/find/" + userIdLong,
                Department.class);

        ResponseTemplateVO vo = new ResponseTemplateVO();
        vo.setStatus(1);
        vo.setMessage("success");
        vo.setDepartment(dept);
        vo.setUserX(userX.getUserX());

        return vo;
    }

    public List<UserX> findAll() {
        return userRepository.findAll();
    }

}
