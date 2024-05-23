package com.harry.userservice.model.res.data;

import lombok.Data;

@Data
public class LocationData {
    int locationId;
    String locationName;
    double latitude;
    double longitude;
    boolean active;
    long createdAt;
}
