package com.jiocoders.taskservice.networkmodel.response;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class ResponseStatusList extends ResponseBase {

    public List<TaskStatus> statusList = new ArrayList<>();
}
