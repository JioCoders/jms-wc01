package com.jiocoders.taskservice.repository;

import com.jiocoders.taskservice.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {

    @Modifying
    @Query(value = "update tbl_task_status u set u.is_active=false where u.status_id=?1", nativeQuery = true)
    void removeStatus(String statusId);

}
