package com.example.demo.model;

import com.example.demo.entity.TaskEntity;

public class Task {

    private Long id;
    private String title, participants, address, time, description;
    private boolean completed, owning;

    public Task() {

    }

    public static Task toModel(TaskEntity taskEntity) {
        Task model = new Task();
        model.setCompleted(taskEntity.isCompleted());
        model.setTitle(taskEntity.getTitle());
        model.setId(taskEntity.getId());
        model.setAddress(taskEntity.getAddress());
        model.setDescription(taskEntity.getDescription());
        model.setTime(taskEntity.getTime());
        model.setParticipants(taskEntity.getParticipants());
        model.setOwning(taskEntity.isOwning());
        return model;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOwning() {
        return owning;
    }

    public void setOwning(boolean owning) {
        this.owning = owning;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}