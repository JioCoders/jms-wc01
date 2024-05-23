package com.jiocoders.empservice.service.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.jiocoders.empservice.entity.Employee;

public interface IEmpService {
    public List<Employee> findAll();

    public List<Employee> findByNameContaining(String name);

    public Optional<Employee> findById(Long id);

    public Employee save(Employee emp);

    public Employee update(Long id, Employee emp);

    public Map<String, Boolean> deleteById(Long id);

    public Map<String, Boolean> deleteAll();

    public List<Employee> findByActive(boolean isActive);
}
