package com.jiocoders.taskservice.service;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskAdd;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskList;
import com.jiocoders.taskservice.networkmodel.request.RequestTaskUpdate;
import com.jiocoders.taskservice.networkmodel.response.ResponseTaskList;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    ResponseEntity<ResponseTaskList> getTaskList(RequestTaskList request, String token);

    ResponseEntity<ResponseBase> addTask(RequestTaskAdd request, String token);

    ResponseEntity<ResponseBase> updateTask(RequestTaskUpdate request, String token);

    ResponseEntity<ResponseBase> removeTask(String taskId, String token);
}
