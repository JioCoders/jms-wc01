package com.jiocoders.taskservice.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.exception.CommonException;
import com.jiocoders.taskservice.networkmodel.request.RequestStatusAdd;
import com.jiocoders.taskservice.networkmodel.response.ResponseStatusList;
import com.jiocoders.taskservice.service.TaskStatusService;
import com.jiocoders.taskservice.utils.ApiConstant;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(ApiConstant.STATUS_CONTROLLER)
public class TaskStatusController {
    private static Logger logger = LoggerFactory.getLogger(TaskStatusController.class);

    @Resource(name = ApiConstant.TASK_STATUS_SERVICE)
    private TaskStatusService taskStatusService;

    /**
     * <p>
     * Get all status data in the system. For production system want to use
     * pagination.
     * </p>
     *
     * @param token
     * @return ResponseStatusList List<TaskStatusData>
     */
    @PostMapping(ApiConstant.LIST)
    public ResponseEntity<ResponseStatusList> getStatusList(@RequestHeader(AUTHORIZATION) String token)
            throws CommonException {
        logger.info("check--------------------------data from -------db");
        return taskStatusService.getStatusList(token);
    }

    /**
     * Post request to create status information in the system.
     *
     * @param statusRequest
     * @return
     */
    @PostMapping(ApiConstant.ADD)
    public ResponseEntity<ResponseBase> addStatus(@RequestBody RequestStatusAdd statusRequest,
            @RequestHeader(AUTHORIZATION) String token) {
        return taskStatusService.addStatus(statusRequest, token);
    }

    /**
     * <p>
     * Deactivate(remove) a status from the system based on the ID. The method
     * mapping is like the getStatus with difference of
     *
     * @param statusId
     * @param token
     * @return
     * @PostMapping and @GetMapping
     *              </p>
     */
    @PostMapping(ApiConstant.REMOVE)
    public ResponseEntity<ResponseBase> removeStatus(@RequestBody String statusId,
            @RequestHeader(AUTHORIZATION) String token) {
        return taskStatusService.removeStatus(statusId, token);
    }

}
