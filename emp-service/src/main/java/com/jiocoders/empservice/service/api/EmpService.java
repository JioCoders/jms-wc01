package com.jiocoders.empservice.service.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiocoders.empservice.entity.Employee;
import com.jiocoders.empservice.repository.EmpRepository;
import com.jiocoders.empservice.service.SequenceGeneratorService;

@Service
public class EmpService implements IEmpService {

  @Autowired
  EmpRepository empRepository;

  @Autowired
  SequenceGeneratorService sequenceGeneratorService;

  public List<Employee> findAll() {
    return empRepository.findAll();
  }

  public List<Employee> findByNameContaining(String name) {
    return empRepository.findByNameContaining(name);
  }

  public Optional<Employee> findById(Long id) {
    return empRepository.findById(id);
  }

  public Employee save(Employee emp) {
    emp.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
    return empRepository.save(emp);
  }

  public Employee update(Long id, Employee emp) {
    Optional<Employee> result = empRepository.findById(id);
    if (result.isPresent()) {
      emp.setId(id);
      return empRepository.save(emp);
    }
    return null;
  }

  public Map<String, Boolean> deleteById(Long id) {
    boolean result = false;
    try {
      empRepository.deleteById(id);
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", result);
    return response;
  }

  public Map<String, Boolean> deleteAll() {
    boolean result = false;
    try {
      empRepository.deleteAll();
      result = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", result);
    return response;
  }

  public List<Employee> findByActive(boolean isActive) {
    return empRepository.findByActive(isActive);
  }
}