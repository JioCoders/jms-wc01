package com.harry.userservice.entity;

import com.harry.userservice.utils.DbConstant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = DbConstant.TABLE_EMPLOYEE)
public class Employee {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer empId;

    @Column(name = "name")
    private String empName;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "company_id")
    private Integer companyId;

}
