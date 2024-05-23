package com.jiocoders.empservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Document(collection = "col_emp")
@Data
public class Employee {

  @Transient
  public static final String SEQUENCE_NAME = "emp_sequence";

  @Id
  private Long id;
  @NotBlank
  @Size(max = 50)
  @Indexed(unique = true)
  private String name;
  private String address;
  private String mobile;
  @NotBlank
  @Size(max = 100)
  @Indexed(unique = true)
  private String email;
  private boolean active;

  public Employee(String name, String address, String mobile, String email, boolean active) {
    this.name = name;
    this.address = address;
    this.mobile = mobile;
    this.email = email;
    this.active = active;
  }

}