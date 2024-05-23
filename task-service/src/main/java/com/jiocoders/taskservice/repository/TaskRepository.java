package com.jiocoders.taskservice.repository;

import com.jiocoders.taskservice.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByProjectId(Integer projectId);

    Task findByTaskId(Integer taskId);

    @Modifying
    @Query(value = "update tbl_task u set u.is_active=false where u.task_id=?1", nativeQuery = true)
    void removeTask(String taskId);

}
