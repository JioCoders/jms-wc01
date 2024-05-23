package com.harry.userservice.model.req;

import lombok.Data;

@Data
public class ReqLocationUpdate {
    int locationId;
    String locationName;
    double latitude;
    double longitude;
}
