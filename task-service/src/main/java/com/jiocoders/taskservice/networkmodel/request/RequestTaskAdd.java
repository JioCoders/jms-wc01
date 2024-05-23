package com.jiocoders.taskservice.networkmodel.request;

import lombok.Data;

@Data
public class RequestTaskAdd {
    String taskTitle;
    String taskDescription;
    int statusId;
    long taskDate;

    int projectId;

}
