package com.harry.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserX {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userIdLong;
    private String firstName;
    private String lastName;
    private String emailId;
    private Long departmentId;
}
