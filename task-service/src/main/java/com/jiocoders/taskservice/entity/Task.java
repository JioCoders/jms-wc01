package com.jiocoders.taskservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer taskId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "task_type")
    private String taskType;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "task_date")
    private Long taskDate;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "company_id")
    private Integer companyId;

}
