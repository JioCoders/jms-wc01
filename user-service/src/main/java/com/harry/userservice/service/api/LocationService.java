package com.harry.userservice.service.api;

import org.springframework.http.ResponseEntity;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.model.req.ReqLocationAdd;
import com.harry.userservice.model.req.ReqLocationUpdate;
import com.harry.userservice.model.res.ResponseCommonDetail;
import com.harry.userservice.model.res.ResponseLocationList;

public interface LocationService {

    ResponseEntity<ResponseLocationList> getLocationList(String token);

    ResponseEntity<ResponseBase> addLocation(ReqLocationAdd request, String token);

    ResponseEntity<ResponseBase> updateLocation(ReqLocationUpdate request, String token);

    ResponseEntity<ResponseCommonDetail> detailLocation(int id, String token);

    ResponseEntity<ResponseBase> removeLocation(int id, String token);
}
