package com.jiocoders.taskservice.service;

import org.springframework.http.ResponseEntity;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.networkmodel.request.RequestProjectAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestProjectUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseProjectList;

public interface ProjectService {

    ResponseEntity<ResponseProjectList> getProjectList(String token);

    ResponseEntity<ResponseBase> addProject(RequestProjectAdd request, String token);

    ResponseEntity<ResponseBase> updateProject(RequestProjectUpdate request, String token);

    ResponseEntity<ResponseBase> removeProject(String projectId, String token);
}
