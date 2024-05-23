package com.harry.userservice.model.res;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.model.res.data.LoginData;

public class ResLogin extends ResponseBase {
    LoginData loginData;

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

}
