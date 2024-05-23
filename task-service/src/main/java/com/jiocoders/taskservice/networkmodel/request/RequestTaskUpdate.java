package com.jiocoders.taskservice.networkmodel.request;

import lombok.Data;

@Data
public class RequestTaskUpdate {
    int taskId;
    String taskTitle;
    String taskDescription;
    int statusId;
    long taskDate;

    int projectId;
    
}
