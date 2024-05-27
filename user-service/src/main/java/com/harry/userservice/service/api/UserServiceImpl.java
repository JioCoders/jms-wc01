package com.harry.userservice.service.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.harry.userservice.VO.Department;
import com.harry.userservice.VO.ResponseTemplateVO;
import com.harry.userservice.controller.UserResponse;
import com.harry.userservice.entity.Location;
import com.harry.userservice.entity.UserX;
import com.harry.userservice.model.req.ReqLogin;
import com.harry.userservice.model.res.ResLogin;
import com.harry.userservice.model.res.data.LocationData;
import com.harry.userservice.model.res.data.LoginData;
import com.harry.userservice.repository.LocationRepository;
import com.harry.userservice.repository.UserRepository;
import com.harry.userservice.security.AuthScope;
import com.harry.userservice.security.JwtService;
import com.harry.userservice.utils.ApiConstant;
import com.harry.userservice.utils.Common;
import com.harry.userservice.utils.StrConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String KEY_USER = "USER";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // private HashOperations hashOperations = redisTemplate.opsForHash();

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private RestTemplate restTemplate;

    // @Autowired
    // private SendOTPService otpService;

    @Override
    public ResponseEntity<ResLogin> loginUser(ReqLogin requestLogin) {
        ResLogin responseLogin = new ResLogin();
        if (!Common.checkNotNull(requestLogin.getEmail()) || !Common.checkNotNull(requestLogin.getPassword())
                || requestLogin.getPassword().length() < 6) {
            responseLogin.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseLogin, HttpStatus.OK);
        }
        Pattern pattern = Pattern.compile(Common.regexEmail);
        Matcher matcher = pattern.matcher(requestLogin.getEmail());
        if (!matcher.matches()) {
            responseLogin.setMessage(StrConstant.INVALID_EMAIL);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(responseLogin, HttpStatus.OK);
        }

        UserX user = userRepository.findFirstByEmailAndPassword(requestLogin.getEmail(),
                requestLogin.getPassword());
        if (user != null) {
            LoginData userData = new LoginData();
            userData.setUserId(user.getUserId());
            userData.setEmailId(user.getEmail());
            userData.setMobileNo(user.getMobile());
            userData.setUserName(user.getName());
            userData.setActive(user.getIsActive());
            userData.setAdmin(user.getIsAdmin());
            userData.setSuperAdmin(user.getIsSuperAdmin());
            userData.setCreatedAt(user.getCreatedAt());
            userData.setUpdatedAt(user.getUpdatedAt());
            Optional<Location> repoLocation = locationRepository.findActiveByLocationId(user.getLocationId());
            LocationData locationData = new LocationData();
            if (repoLocation.isPresent()) {
                Location location = repoLocation.get();
                locationData.setLocationId(location.getLocationId());
                locationData.setLocationName(location.getLocationName());
                locationData.setLatitude(location.getLatitude());
                locationData.setLongitude(location.getLongitude());
                locationData.setActive(location.getIsActive());
                locationData.setCreatedAt(location.getCreatedAt());
                userData.setLocationData(locationData);
            } else {
                userData.setLocationData(null);
            }
            try {
                AuthScope scope;
                String token = null;
                if (user.getIsAdmin()) {
                    scope = AuthScope.ADMIN;
                } else if (user.getIsSuperAdmin()) {
                    scope = AuthScope.SUPER_ADMIN;
                } else {
                    scope = AuthScope.USER;
                }
                token = jwtService.generateToken(user, scope);
                userData.setToken(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // String otpString = String.valueOf(Common.generateOTP());
            // emailService.sendSMS(user.getMobileNo(), otpString);
            // emailService.sendEmailOTP(user.getEmailId(), otpString);
            // user.setOtp(otpString);

            responseLogin.setLoginData(userData);
            responseLogin.setMessage(StrConstant.SUCCESS);
            responseLogin.setStatus(ApiConstant.SUCCESS_CODE);
        } else {
            responseLogin.setMessage(StrConstant.PLEASE_PROVIDE_VALID_EMAIL_PASSWORD);
            responseLogin.setStatus(ApiConstant.INVALID_REQUEST_CODE);
        }
        return new ResponseEntity<>(responseLogin, HttpStatus.OK);
    }

    @Override
    public UserResponse saveUser(UserX userX) {
        log.info("UserService =======> findByUserId()" + userX.toString());
        UserResponse res = new UserResponse();
        userX.setCompanyId(1);
        userX.setCreatedAt(System.currentTimeMillis());
        userX.setUpdatedAt(System.currentTimeMillis());
        UserX user;
        try {
            user = userRepository.save(userX);
            res.setCode(1);
            res.setMessage("Success");
            res.setUserX(user);

            redisTemplate.opsForHash().put(KEY_USER, user.getUserId().toString(), user);
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @Override
    public UserResponse findByUserId(int userId) {
        log.info("UserService =======> findByUserId()" + userId);
        UserResponse res = new UserResponse();
        UserX userX2 = null;
        try {
            // userX2 = (UserX) redisTemplate.opsForHash().get(KEY_USER,
            //         String.valueOf(userId));

            if (userX2 == null) {
                userX2 = userRepository.findByUserId(userId);
            }
            res.setCode(1);
            res.setMessage("Success");
            res.setUserX(userX2);
        } catch (Exception e) {
            res.setCode(-1);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @Override
    public ResponseTemplateVO getUserWithDepartment(int userId) {
        log.info("UserService =======> getUserWithDepartment()=" + userId);

        UserResponse userX = findByUserId(userId);
        Department dept = restTemplate.getForObject("http://DEPARTMENT-SERVICE/dept/find/" + userId,
                Department.class);

        ResponseTemplateVO vo = new ResponseTemplateVO();
        vo.setStatus(1);
        vo.setMessage("success");
        vo.setDepartment(dept);
        vo.setUserX(userX.getUserX());

        return vo;
    }

    @Override
    public List<UserX> findAll() {
        // try {
        // otpService.sendOTPEmail("jiocoders@gmail.com", "223344");

        // Car m = new Car();
        // m.start();
        // m.automate(true);
        // } catch (Exception | VehicleException e) {
        // System.out.println("Error: " + e.getMessage());
        // }

        List<UserX> users = new ArrayList<>();
        try {
            List<Object> objects = redisTemplate.opsForHash().values(KEY_USER);
            // List<UserX> objects = redisTemplate.opsForHash().entries(KEY_USER);
            users = objects.stream()
                    .map(element -> (UserX) element)
                    .collect(Collectors.toList());

            // result = redisTemplate.opsForHash().get(KEY_USER, "554");
            // log.info("KEY_USER------------>" + result);
        } catch (Exception e) {
            log.info("KEY_USER------------>" + e.getMessage());
        }
        return users;
    }

}
