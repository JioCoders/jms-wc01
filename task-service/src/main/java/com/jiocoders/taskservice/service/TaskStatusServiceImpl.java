package com.jiocoders.taskservice.service;

import static com.jiocoders.taskservice.utils.ApiConstant.BAD_TOKEN_CODE;
import static com.jiocoders.taskservice.utils.ApiConstant.TASK_STATUS_SERVICE;
import static com.jiocoders.taskservice.utils.Common.checkNotNull;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.TaskStatus;
import com.jiocoders.taskservice.entity.UserX;
import com.jiocoders.taskservice.networkmodel.request.RequestStatusAdd;
import com.jiocoders.taskservice.networkmodel.response.ResponseStatusList;
import com.jiocoders.taskservice.repository.TaskStatusRepository;
import com.jiocoders.taskservice.repository.UserRepository;
import com.jiocoders.taskservice.security.AuthScope;
import com.jiocoders.taskservice.security.JwtTokenProvider;
import com.jiocoders.taskservice.utils.ApiConstant;
import com.jiocoders.taskservice.utils.StrConstant;

@Service(TASK_STATUS_SERVICE)
// @CacheConfig(cacheNames = {"Status"})
public class TaskStatusServiceImpl implements TaskStatusService {
    private static final Logger logger = LoggerFactory.getLogger(TaskStatusServiceImpl.class);
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository adminRepository;

    /**
     * Method to return the list of all the status in the system. This is an
     * implementation but use pagination in the real world.
     *
     * @param token
     * @return list of status
     */
    @Override
    // @Cacheable(value = "status_s", key = "#id")
    @Cacheable(value = "status_s")
    public ResponseEntity<ResponseStatusList> getStatusList(String token) {
        ResponseStatusList response = new ResponseStatusList();
        if (!checkNotNull(token)) {
            response.setMessage(StrConstant.TOKEN_NOT_FOUND);
            response.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (!jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                && !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            response.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            response.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        // CHECK VALID USER ID
        int loginUserId = Integer.parseInt(jwtTokenProvider.extractUserId(bearer));
        logger.info("UserId={}", loginUserId);
        Optional<UserX> result = adminRepository.findById(loginUserId);
        if (result.isEmpty()) {
            response.setMessage("User not found!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        UserX employee = result.get();
        if (!employee.getIsActive()) {
            response.setMessage("User is not active to update!");
            response.setStatus(ApiConstant.INVALID_REQUEST_CODE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // List<Option> optionRedis = redisTemplate.opsForHash().values(KEY);
        // logger.info("+++++++++++++Executing query to fetch option list+++++++++++++"
        // + optionRedis.size());
        List<TaskStatus> tasks = taskStatusRepository.findAll();
        response.statusList.addAll(tasks);
        response.setMessage(StrConstant.SUCCESS);
        response.setStatus(ApiConstant.SUCCESS_CODE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a status based on the data sent to the service class.
     *
     * @param statusRequest
     * @param token
     * @return DTO representation of the status
     */
    @Override
    @CacheEvict(value = "status_s", allEntries = true)
    public ResponseEntity<ResponseBase> addStatus(RequestStatusAdd statusRequest, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.FORBIDDEN);
        }

        // CHECK VALIDATION
        // UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));
        if (!checkNotNull(statusRequest.getStatusName()) || statusRequest.getStatusName().length() < 2) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Task status name");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        List<TaskStatus> statusResult = taskStatusRepository.findAll();
        boolean isExist = false;
        for (TaskStatus t : statusResult) {
            if (t.getStatusName().equalsIgnoreCase(statusRequest.getStatusName())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            responseBase.setMessage(statusRequest.getStatusName() + " - task status is already exists!");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }
        TaskStatus status = new TaskStatus();
        status.setStatusName(statusRequest.getStatusName());
        status.setIsActive(true);
        taskStatusRepository.save(status);
        // try {
        // redisTemplate.opsForHash().put(KEY, option.getOptionId(), option);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }

    /**
     * Delete status based on the status ID. We can also use other option to delete
     * status
     * based on the entity (passing JPA entity class as method parameter)
     *
     * @param statusId
     * @param token
     * @return boolean flag showing the request status
     */
    @Override
    @CacheEvict(value = "status_s", allEntries = true)
    public ResponseEntity<ResponseBase> removeStatus(String statusId, String token) {
        ResponseBase responseBase = new ResponseBase();
        if (!checkNotNull(token)) {
            responseBase.setMessage(StrConstant.TOKEN_NOT_FOUND);
            responseBase.setStatus(BAD_TOKEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.UNAUTHORIZED);
        }

        // CHECK ADMIN USER
        String bearer = jwtTokenProvider.resolveToken(token);
        if (jwtTokenProvider.validateToken(bearer, AuthScope.USER)
                || !jwtTokenProvider.validateToken(bearer, AuthScope.ADMIN)) {
            responseBase.setMessage(StrConstant.UN_AUTHORISE_ACCESS);
            responseBase.setStatus(ApiConstant.FORBIDDEN_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.FORBIDDEN);
        }

        // CHECK VALIDATION
        // UUID userId = UUID.fromString(jwtTokenProvider.getUsername(bearer));
        if (!checkNotNull(statusId) || statusId.length() < 10) {
            responseBase.setMessage(StrConstant.PLEASE_PROVIDE_MANDATORY_FIELD + ": Status id");
            responseBase.setStatus(ApiConstant.BAD_REQUEST_CODE);
            return new ResponseEntity<>(responseBase, HttpStatus.BAD_REQUEST);
        }

        taskStatusRepository.removeStatus(statusId);
        responseBase.setMessage(StrConstant.INSERT_SUCCESS);
        responseBase.setStatus(ApiConstant.SUCCESS_INSERT_CODE);

        return new ResponseEntity<>(responseBase, HttpStatus.OK);
    }
}
