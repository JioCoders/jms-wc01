package com.harry.userservice.model.res.data;

import lombok.Data;

@Data
public class LoginData {
    int userId;
    String userName;
    String emailId;
    String mobileNo;
    LocationData locationData;
    boolean isActive;
    boolean isAdmin;
    boolean isSuperAdmin;
    Long createdAt;
    Long updatedAt;
    int companyId;
    String token;
}
