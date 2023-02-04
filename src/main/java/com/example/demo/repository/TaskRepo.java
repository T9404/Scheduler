package com.example.demo.repository;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepo extends CrudRepository<TaskEntity, Long> {
    TaskEntity findByTimeAndUser(String time, UserEntity user);
}
