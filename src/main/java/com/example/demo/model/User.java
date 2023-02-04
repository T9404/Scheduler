package com.example.demo.model;

import com.example.demo.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class User {

    private Long id;
    private String username;
    private String email;
    private List<Task> taskList;

    public User() {

    }

    public static User toModel(UserEntity entity) {
        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setTaskList(entity.getTasks().stream().map(Task::toModel).collect(Collectors.toList()));
        return model;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}