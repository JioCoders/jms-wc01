package com.jiocoders.empservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.jiocoders.empservice.entity.Employee;
import com.jiocoders.empservice.service.api.EmpService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskRunnable implements CommandLineRunner {

    @Autowired
    EmpService emplyeeService;

    @Override
    public void run(String... args) throws Exception {
        log.info("---------> Inserting dummy data in Employee table");
        emplyeeService.save(new Employee("Ramesh", "Fadatare", "8888888889", "ramesh@gmail.com", true));
        emplyeeService.save(new Employee("Tom", "Cruise", "8888888889", "tom@gmail.com", true));
        emplyeeService.save(new Employee("John", "Cena", "8888888889", "john@gmail.com", true));
        emplyeeService.save(new Employee("tony", "stark", "8888888889", "stark@gmail.com", true));

        long id1 = 1L;
        emplyeeService.findById(id1).ifPresent(System.out::println);

        long id2 = 5L;
        Optional<Employee> optional = emplyeeService.findById(id2);

        if (optional.isPresent()) {
            System.out.println(optional.get());
        } else {
            System.out.printf("No employee found with id %d%n", id2);
        }

        List<Employee> employees = emplyeeService.findAll();
        employees.forEach(employee -> System.out.println(employee.toString()));
        log.info("---------> Initial data inserted in Employee table");

        // emplyeeService.deleteById(3L);
    }

}
