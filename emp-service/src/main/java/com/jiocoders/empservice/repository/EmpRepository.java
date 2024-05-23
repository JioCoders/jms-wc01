package com.jiocoders.empservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jiocoders.empservice.entity.Employee;

@Repository
public interface EmpRepository extends MongoRepository<Employee, Long> {
  List<Employee> findByActive(boolean published);

  List<Employee> findByNameContaining(String title);
}