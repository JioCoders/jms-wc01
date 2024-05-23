package com.jiocoders.taskservice.repository;

import com.jiocoders.taskservice.entity.Project;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update tbl_project u set u.is_active=false where u.project_id=?1", nativeQuery = true)
    void removeProject(String projectId);

}
