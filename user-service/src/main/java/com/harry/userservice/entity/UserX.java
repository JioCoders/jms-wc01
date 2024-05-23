package com.harry.userservice.entity;

import java.util.Set;

import com.harry.userservice.security.AuthScope;
import com.harry.userservice.utils.DbConstant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = DbConstant.TABLE_USER)
public class UserX {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer userId;
    private String name;
    private String email;
    private String mobile;
    @Column(name = "location_id")
    private Integer locationId;
    private String pincode;
    private String otp;
    private String password;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "created_at")
    private Long createdAt;
    @Column(name = "updated_at")
    private Long updatedAt;
    @Column(name = "company_id")
    private Integer companyId;
    private Set<AuthScope> authorities;
}
