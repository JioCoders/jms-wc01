package com.jiocoders.taskservice.entity;

import java.io.Serializable;

import com.jiocoders.taskservice.utils.DbConstant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = DbConstant.TABLE_STATUS)
public class TaskStatus implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer statusId;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "is_active")
    private Boolean isActive;
}
