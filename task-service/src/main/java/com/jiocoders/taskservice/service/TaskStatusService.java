package com.jiocoders.taskservice.service;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.networkmodel.request.RequestStatusAdd;
import com.jiocoders.taskservice.networkmodel.response.ResponseStatusList;
import org.springframework.http.ResponseEntity;

public interface TaskStatusService {

    ResponseEntity<ResponseStatusList> getStatusList(String token);

    ResponseEntity<ResponseBase> addStatus(RequestStatusAdd request, String token);

    ResponseEntity<ResponseBase> removeStatus(String statusId, String token);
}
