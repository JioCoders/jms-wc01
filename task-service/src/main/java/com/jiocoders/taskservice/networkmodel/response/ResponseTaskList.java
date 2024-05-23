package com.jiocoders.taskservice.networkmodel.response;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class ResponseTaskList extends ResponseBase {

    public List<Task> taskList = new ArrayList<>();
}
