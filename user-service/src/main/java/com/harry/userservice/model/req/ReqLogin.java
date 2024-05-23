package com.harry.userservice.model.req;

import lombok.Data;

@Data
public class ReqLogin {
    String email;
    String password;
}
