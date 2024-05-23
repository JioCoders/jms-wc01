package com.harry.userservice.model.req;

import lombok.Data;

@Data
public class ReqLocationAdd {
    String locationName;
    double latitude;
    double longitude;
    int companyId;

}
