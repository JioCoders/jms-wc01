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
import com.jiocoders.taskservice.networkmodel.request.RequestProjectAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestProjectUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseProjectList;
import com.jiocoders.taskservice.service.ProjectService;
import com.jiocoders.taskservice.utils.ApiConstant;

import jakarta.annotation.Resource;

@RestController
@RequestMapping(ApiConstant.PROJECT_CONTROLLER)
public class ProjectController {
    private static Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Resource(name = ApiConstant.PROJECT_SERVICE)
    private ProjectService projectService;

    /**
     * <p>
     * Get all PROJECT data in the system.For production system want to use
     * pagination.
     * </p>
     *
     * @param token
     * @return ResponsePROJECTList List<CustomerData>
     */
    @PostMapping(ApiConstant.LIST)
    public ResponseEntity<ResponseProjectList> getProjectList(@RequestHeader(AUTHORIZATION) String token) {
        logger.info("check--------------------------data from -------db");
        return projectService.getProjectList(token);
    }

    /**
     * Post request to create project information in the system.
     *
     * @param projectRequest
     * @return
     */
    @PostMapping(ApiConstant.ADD)
    public ResponseEntity<ResponseBase> addProject(@RequestBody RequestProjectAdd projectRequest,
            @RequestHeader(AUTHORIZATION) String token) {
        return projectService.addProject(projectRequest, token);
    }

    /**
     * Post request to create project information in the system.
     *
     * @param projectRequest
     * @return
     */
    @PostMapping(ApiConstant.UPDATE)
    public ResponseEntity<ResponseBase> updateProject(@RequestBody RequestProjectUpdate projectRequest,
            @RequestHeader(AUTHORIZATION) String token) {
        return projectService.updateProject(projectRequest, token);
    }

    /**
     * <p>
     * Deactivate(remove) a project from the system based on the ID. The method
     * mapping is like the getProject with difference of
     *
     * @param projectId
     * @param token
     * @return
     * @PostMapping and @GetMapping
     *              </p>
     */
    @PostMapping(ApiConstant.REMOVE)
    public ResponseEntity<ResponseBase> removeProject(@RequestBody String projectId,
            @RequestHeader(AUTHORIZATION) String token) {
        return projectService.removeProject(projectId, token);
    }

}
