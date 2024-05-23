package com.jiocoders.taskservice.networkmodel.request;

import java.util.UUID;

public class RequestProjectUpdate {
    UUID projectId;
    String projectName;
    String projectDescription;

    String projectLeadEmpId;

    String projectManagerEmpId;

    public UUID getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectLeadEmpId() {
        return projectLeadEmpId;
    }

    public String getProjectManagerEmpId() {
        return projectManagerEmpId;
    }
}
