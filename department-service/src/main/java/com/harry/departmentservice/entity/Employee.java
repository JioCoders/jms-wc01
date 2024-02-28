package com.harry.departmentservice.entity;

public record Employee(
        // @Id @GeneratedValue(strategy = GenerationType.AUTO) Long employeeId,
        Long departmentId,
        String name,
        int age,
        String position) {

}
