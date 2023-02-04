package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String participants;
    private String address;
    private String time;
    private String description;
    private  boolean completed;
    private boolean owning;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public TaskEntity() {

    }

    public TaskEntity(TaskEntity temp) {
        this.participants = temp.getParticipants();
        this.title = temp.getTitle();
        this.time = temp.getTime();
        this.address = temp.getAddress();
        this.user = temp.getUser();
        this.completed = temp.isCompleted();
        this.owning = false;
        this.description = temp.getDescription();
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public void changeTitle(TaskEntity changedTask, TaskEntity task) {
        if (changedTask.getTitle() != null) {
            task.setTitle(changedTask.getTitle());
        }
    }

    public void changeDescription(TaskEntity changedTask, TaskEntity task) {
        if (changedTask.getDescription() != null) {
            task.setDescription(changedTask.getDescription());
        }
    }

    public void changeParticipants(TaskEntity changedTask, TaskEntity task) {
        if (changedTask.getParticipants() != null) {
            task.setParticipants(changedTask.getParticipants());
        }
    }

    public void changeTime(TaskEntity changedTask, TaskEntity task) {
        if (changedTask.getTime() != null) {
            task.setTime(changedTask.getTime());
        }
    }

    public void changeAddress(TaskEntity changedTask, TaskEntity task) {
        if (changedTask.getAddress() != null) {
            task.setAddress(changedTask.getAddress());
        }
    }
}