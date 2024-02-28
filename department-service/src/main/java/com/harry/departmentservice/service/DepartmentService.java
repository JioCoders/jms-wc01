package com.harry.departmentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harry.departmentservice.entity.Department;
import com.harry.departmentservice.repository.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public Department saveDepartment(Department department) {
        log.info("DepartmentService[] => saveDepartment(department)");
        return departmentRepository.save(department);
    }

    public Department findByDepartmentId(Long departmentId) {
        log.info("DepartmentService[] => findByDepartmentId(departmentId)");
        return departmentRepository.findByDepartmentId(departmentId);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

}
