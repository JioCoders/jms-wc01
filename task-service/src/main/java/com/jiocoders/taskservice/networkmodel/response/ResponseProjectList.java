package com.jiocoders.taskservice.networkmodel.response;

import com.jiocoders.taskservice.base.ResponseBase;
import com.jiocoders.taskservice.entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ResponseProjectList extends ResponseBase {

    public List<Project> projectList = new ArrayList<>();
}
