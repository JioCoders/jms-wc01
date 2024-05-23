package com.harry.userservice.controller;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.model.req.ReqLocationAdd;
import com.harry.userservice.model.req.ReqLocationUpdate;
import com.harry.userservice.model.res.ResponseCommonDetail;
import com.harry.userservice.model.res.ResponseLocationList;
import com.harry.userservice.service.api.LocationService;
import com.harry.userservice.utils.ApiConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import static com.harry.userservice.utils.ApiConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/location")
@CrossOrigin(origins = "*")
public class LocationController {
    private static Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Resource(name = LOCATION_SERVICE)
    private LocationService locationService;

    /**
     * <p>
     * Get all location data in the system.For production system want to use
     * pagination.
     * </p>
     * removed this feature
     *
     * @param token
     * @return ResponseLocationList List<LocationData>
     */
    @PostMapping(ApiConstant.LIST)
    public ResponseEntity<ResponseLocationList> getLocationList(@RequestHeader(AUTHORIZATION) String token) {
        logger.info("check data from -------db");
        return locationService.getLocationList(token);
    }

    /**
     * Post request to create location information in the system.
     *
     * @param locationRequest
     * @return
     */
    @PostMapping(value = ApiConstant.ADD, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    public ResponseEntity<ResponseBase> addLocation(@RequestBody ReqLocationAdd locationRequest,
            @RequestHeader(AUTHORIZATION) String token) {
        return locationService.addLocation(locationRequest, token);
    }

    /**
     * Post request to update location information in the system.
     *
     * @param request
     * @return
     */
    @PostMapping(UPDATE)
    public ResponseEntity<ResponseBase> updateLocation(@RequestBody ReqLocationUpdate request,
            @RequestHeader(AUTHORIZATION) String token) {
        return locationService.updateLocation(request, token);
    }

    /**
     * Post request to update location information in the system.
     *
     * @param request
     * @return
     */
    @PostMapping(REMOVE)
    public ResponseEntity<ResponseBase> removeLocation(@RequestBody int id,
            @RequestHeader(AUTHORIZATION) String token) {
        return locationService.removeLocation(id, token);
    }

    /**
     * Post request to get location details information in the system.
     *
     * @param id
     * @return
     */
    @PostMapping(DETAIL)
    public ResponseEntity<ResponseCommonDetail> detailLocation(@RequestBody int id,
            @RequestHeader(AUTHORIZATION) String token) {
        return locationService.detailLocation(id, token);
    }
}
