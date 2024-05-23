package com.jiocoders.taskservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskList;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseTaskList;
import com.jiocoders.taskservice.service.TaskService;
import com.jiocoders.taskservice.utils.ApiConstant;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(ApiConstant.TASK_CONTROLLER)
public class TaskController {
    private static Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Resource(name = ApiConstant.TASK_SERVICE)
    private TaskService taskService;

    /**
     * <p>
     * Get all task data in the system. For production system want to use
     * pagination.
     * </p>
     *
     * @param token
     * @param request [projectId]
     * @return ResponseTaskList List<TaskDatoa>
     */
    @PostMapping(ApiConstant.LIST)
    public ResponseEntity<ResponseTaskList> getTaskList(@RequestBody RequestTaskList request,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        logger.info("check--------------------------data from -------db");
        return taskService.getTaskList(request, token);
    }

    /**
     * Post request to create task information in the system.
     *
     * @param taskRequest
     * @return
     */
    @PostMapping(ApiConstant.ADD)
    public ResponseEntity<ResponseBase> addTask(@RequestBody RequestTaskAdd taskRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return taskService.addTask(taskRequest, token);
    }

    /**
     * Post request to update task information in the system.
     *
     * @param taskRequest
     * @return
     */
    @PostMapping(ApiConstant.UPDATE)
    public ResponseEntity<ResponseBase> updateTask(@RequestBody RequestTaskUpdate taskRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return taskService.updateTask(taskRequest, token);
    }

    /**
     * <p>
     * Deactivate(remove) a task from the system based on the ID. The method
     * mapping
     * is like the getTask with difference of
     *
     * @param taskId
     * @param token
     * @return
     * @PostMapping and @GetMapping
     *              </p>
     */
    @PostMapping(ApiConstant.REMOVE)
    public ResponseEntity<ResponseBase> removeTask(@RequestBody String taskId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return taskService.removeTask(taskId, token);
    }

}
