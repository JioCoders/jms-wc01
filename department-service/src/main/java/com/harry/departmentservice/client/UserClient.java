package com.harry.departmentservice.client;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface UserClient {

    @GetExchange("/user/findByUserId/{id}")
    public UserResponse findByUserId(@PathVariable("id") Long userIdLong);

    @GetExchange("/user/findAll")
    public List<UserX> findAllUsers();
}
