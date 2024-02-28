package com.harry.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harry.userservice.entity.UserX;

@Repository
public interface UserRepository extends JpaRepository<UserX, Long>{

    UserX findByuserIdLong(Long userIdLong);
    
}
