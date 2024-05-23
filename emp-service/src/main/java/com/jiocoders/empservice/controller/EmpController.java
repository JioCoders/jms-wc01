package com.jiocoders.empservice.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jiocoders.empservice.entity.Employee;
import com.jiocoders.empservice.service.api.IEmpService;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EmpController {

  @Autowired
  IEmpService iService;

  @GetMapping("/emp")
  @ResponseStatus(HttpStatus.OK)
  public List<Employee> getAllEmp(@RequestParam(required = false) String title) {
    if (title == null)
      return iService.findAll();
    else
      return iService.findByNameContaining(title);
  }

  @GetMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Optional<Employee> getEmployeeById(@PathVariable("id") Long id) {
    return iService.findById(id);
  }

  @PostMapping("/emp")
  @ResponseStatus(HttpStatus.CREATED)
  public Employee createEmployee(@RequestBody Employee emp) {
    return iService
        .save(new Employee(emp.getName(), emp.getAddress(), emp.getMobile(), emp.getEmail(), true));
  }

  @PutMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Employee updateEmp(@PathVariable("id") long id,
      @RequestBody Employee emp) {
    return iService.update(id, emp);
  }

  @DeleteMapping("/emp/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Boolean> deleteEmployee(@PathVariable("id") long id) {
    return iService.deleteById(id);
  }

  @DeleteMapping("/emp")
  @ResponseStatus(HttpStatus.OK)
  public Map<String, Boolean> deleteAllemp() {
    return iService.deleteAll();
  }

  @GetMapping("/emp/active")
  @ResponseStatus(HttpStatus.OK)
  public List<Employee> findByActive() {
    return iService.findByActive(true);
  }
}