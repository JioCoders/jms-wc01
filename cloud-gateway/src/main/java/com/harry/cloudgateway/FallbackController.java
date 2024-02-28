package com.harry.cloudgateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/userServiceFallback")
    public String userServiceFallback() {
        return "User service is taking longer than expected, please try again later";
    }

    @GetMapping("/deptServiceFallback")
    public String deptServiceFallback() {
        return "Dept service is taking longer than expected, please try again later";
    }

}
