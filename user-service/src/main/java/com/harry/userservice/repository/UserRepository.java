package com.harry.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harry.userservice.entity.UserX;

@Repository
public interface UserRepository extends JpaRepository<UserX, Integer>{

    UserX findByUserId(int userId);

    UserX findFirstByEmailAndPassword(String emailId, String password);

    // @Query(value = "select * from tbl_user", nativeQuery = true)
    // List<User> selectAllUsers();

    // @Query(value = "select * from tbl_user where is_active=true", nativeQuery =
    // true)
    // List<Employee> getActiveEmployeeList();
    //
    // @Query(value = "select * from tbl_user where id != ?1 and is_active=true",
    // nativeQuery = true)
    // List<Employee> getActiveEmployeeById(UUID id);

}
