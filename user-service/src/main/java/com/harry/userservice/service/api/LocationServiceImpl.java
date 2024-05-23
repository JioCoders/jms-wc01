package com.harry.userservice.service.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.harry.userservice.base.ResponseBase;
import com.harry.userservice.entity.Location;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.exception.CommonException;
import com.harry.userservice.model.req.ReqLocationAdd;
import com.harry.userservice.model.req.ReqLocationUpdate;
import com.harry.userservice.model.res.ResponseCommonDetail;
import com.harry.userservice.model.res.ResponseLocationList;
import com.harry.userservice.model.res.data.CommonData;
import com.harry.userservice.model.res.data.LocationData;
import com.harry.userservice.repository.LocationRepository;
import com.harry.userservice.repository.UserRepository;
import com.harry.userservice.security.AuthScope;
import com.harry.userservice.security.JwtService;
import com.harry.userservice.utils.ApiConstant;
import com.harry.userservice.utils.Common;
import com.harry.userservice.utils.StrConstant;

@Service(ApiConstant.LOCATION_SERVICE)
public class LocationServiceImpl implements LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    private static final String serviceName = "location";

    // @Autowired
    // private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtService jwtService;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    private UserRepository adminRepository;

    /**
     * Method to return the list of all the location in the system. This is an
     * implementation but use pagination in the real world.
     *
     * @param token
     * @return list of locations
     */
    @Override
    public ResponseEntity<ResponseLocationList> getLocationList(String token) {
        ResponseLocationList response = new ResponseLocationList();
        if (!Common.checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtService.resolveToken(token);
        if (!jwtService.validateToken(bearer, AuthScope.USER)
                && !jwtService.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtService.extractUserId(bearer));
        logger.info("UserId={}", loginUserId);
        UserX user = adminRepository.findById(loginUserId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.getIsActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<Location> locations = locationRepository.findAllActive();
        List<LocationData> dataList = new ArrayList<>();
        for (Location loc : locations) {
            LocationData d = new LocationData();
            d.setLocationId(loc.getLocationId());
            d.setLocationName(loc.getLocationName());
            d.setLatitude(loc.getLatitude());
            d.setLongitude(loc.getLongitude());
            d.setActive(loc.getIsActive());
            d.setCreatedAt(loc.getCreatedAt());
            dataList.add(d);
        }
        response.setLocationList(dataList);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a location based on the data sent to the service class.
     *
     * @param request
     * @param token
     * @return DTO representation of the location
     */
    @Override
    public ResponseEntity<ResponseBase> addLocation(ReqLocationAdd request, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!Common.checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtService.resolveToken(token);
        if (!jwtService.validateToken(bearer, AuthScope.USER)
                && !jwtService.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        // CHECK VALIDATION
        int loginUserId = Integer.parseInt(jwtService.extractUserId(bearer));
        UserX loginUser = adminRepository.findById(loginUserId).get();
        if (loginUser.getCompanyId() == 0) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK location name
        if (!Common.checkNotNull(request.getLocationName()) || request.getLocationName().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": " + serviceName + " name");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        List<Location> repoResult = locationRepository.findAll();
        boolean isExist = false;
        for (Location w : repoResult) {
            if (w.getLocationName().equalsIgnoreCase(request.getLocationName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(request.getLocationName() + " - " + serviceName + " is already exists!");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        Location location = new Location();
        if (Common.checkNotNull(request.getLocationName())) {
            location.setLocationName(request.getLocationName());
        }
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());

        location.setCompanyId(loginUser.getCompanyId());
        location.setIsActive(true);
        location.setCreatedAt(System.currentTimeMillis());
        locationRepository.save(location);

        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> updateLocation(ReqLocationUpdate request, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!Common.checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtService.resolveToken(token);
        if (!jwtService.validateToken(bearer, AuthScope.USER)
                && !jwtService.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }

        // CHECK VALIDATION
        // int userId = Integer.parseInt(jwtTokenProvider.getUsername(bearer));
        if (request.getLocationId() == 0) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE + " : " + serviceName + " id");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        if (!Common.checkNotNull(request.getLocationName())) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE + " : " + serviceName + " name");
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        Optional<Location> locationRepo = locationRepository.findActiveByLocationId(request.getLocationId());
        if (locationRepo.isEmpty()) {
            responseBase.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        Location location = locationRepo.get();
        if (location.getLocationName().equals(request.getLocationName())) {
            responseBase.setMessage(StrConstant.DUPLICATE_ENTRY);
            responseBase.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.OK);
        }
        location.setLocationName(request.getLocationName());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());

        locationRepository.saveAndFlush(location);

        responseBase.setMessage(StrConstant.UPDATE_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseCommonDetail> detailLocation(int id, String token) {
        ResponseCommonDetail response = new ResponseCommonDetail();
        if (!Common.checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtService.resolveToken(token);
        if (!jwtService.validateToken(bearer, AuthScope.USER)
                && !jwtService.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtService.extractUserId(bearer));
        logger.info("UserId={}", loginUserId);
        UserX user = adminRepository.findById(loginUserId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.getIsActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Optional<Location> repoLocation = locationRepository.findActiveByLocationId(id);
        if (repoLocation.isEmpty()) {
            throw new CommonException(serviceName + " details are not found", null);
        }
        Location location = repoLocation.get();
        CommonData c = new CommonData();
        c.setId(location.getLocationId());
        c.setName(location.getLocationName());
        response.setData(c);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseBase> removeLocation(int id, String token) {
        ResponseBase response = new ResponseBase();
        if (!Common.checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // CHECK ADMIN USER
        String bearer = jwtService.resolveToken(token);
        if (!jwtService.validateToken(bearer, AuthScope.USER)
                && !jwtService.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtService.extractUserId(bearer));
        logger.info("UserId={}", loginUserId);
        UserX user = adminRepository.findById(loginUserId).get();
        if (user == null) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!user.getIsActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        Optional<Location> repoLocation = locationRepository.findById(id);

        if (repoLocation.isEmpty()) {
            response.setMessage(serviceName + " details are not found");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        int result = locationRepository.deactiveLocation(id);

        if (result == 1) {
            response.setMessage(StrConstant.UPDATE_SUCCESS);
            response.setStatus(ApiConstant.SUCCESS_CODE);

        } else {
            response.setMessage(StrConstant.ITEM_NOT_FOUND_UPDATE);
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
