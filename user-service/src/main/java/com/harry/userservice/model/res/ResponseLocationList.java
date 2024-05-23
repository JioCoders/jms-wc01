package com.harry.userservice.model.res;

import java.util.ArrayList;
import java.util.List;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.model.res.data.LocationData;

public class ResponseLocationList extends ResponseBase {

    List<LocationData> locationList = new ArrayList<>();

    public List<LocationData> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<LocationData> locationList) {
        this.locationList = locationList;
    }
}
