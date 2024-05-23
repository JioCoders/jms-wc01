package com.jiocoders.taskservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiocoders.taskservice.entity.UserX;

@Repository
public interface UserRepository extends JpaRepository<UserX, Integer> {

    // Optional<UserX> findByUserId(Integer userId);

}
