package com.jiocoders.taskservice.networkmodel.request;

public class RequestProjectAdd {
    String projectName;
    String projectDescription;

    String projectLeadEmpId;

    String projectManagerEmpId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectLeadEmpId() {
        return projectLeadEmpId;
    }

    public void setProjectLeadEmpId(String projectLeadEmpId) {
        this.projectLeadEmpId = projectLeadEmpId;
    }

    public String getProjectManagerEmpId() {
        return projectManagerEmpId;
    }

    public void setProjectManagerEmpId(String projectManagerEmpId) {
        this.projectManagerEmpId = projectManagerEmpId;
    }
}
