package com.example.demo.service;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TimeEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.*;
import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TaskRepo taskRepo;

    public Task createTask(TaskEntity task, Long userId)
            throws OverflowTaskException, UserNotFoundException, InvalidTaskIntervalException {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        // Событие создается раз в час (12:00, 13:00…)
        if (taskRepo.findByTimeAndUser(task.getTime(), user) != null) {
            throw new OverflowTaskException("На 1 час может быть только 1 событие");
        }
        if ((task.getTime().charAt(3) != '0') || (task.getTime().charAt(4) != '0')) {
            throw new InvalidTaskIntervalException("Выберите задачу с промежутком в 1 час");
        }

        task.setUser(user);
        return Task.toModel(taskRepo.save(task));
    }

    public void suggestTaskToUser(String username, Long taskId) throws OverflowTaskException,
            UserNotFoundException, TaskNotFoundException, InvalidTaskIntervalException {

        UserEntity user = userRepo.findByUsername(username);
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена!"));
        if (user == null) {
            throw new UserNotFoundException("Пользователь не найден!");
        }
        if (taskRepo.findByTimeAndUser(task.getTime(), user) != null) {
            throw new OverflowTaskException("На 1 час может быть только 1 событие");
        }

        TaskEntity suggestedTask = new TaskEntity(task);
        createTask(suggestedTask, user.getId());
    }

    public Task completeTask(Long taskId) throws TaskNotFoundException, AccessTaskException {
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена!"));
        if (!task.isOwning()) {
            throw new AccessTaskException("Редактировать можно только собственную задачу");
        }
        task.setCompleted(!task.isCompleted());
        return Task.toModel(taskRepo.save(task));
    }

    public Long deleteTask(Long taskId) throws TaskNotFoundException, AccessTaskException {
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена!"));
        if (!task.isOwning()) {
            throw new AccessTaskException("Удалять можно только собственную задачу");
        }
        taskRepo.deleteById(taskId);
        return taskId;
    }

    public Task changeTask(TaskEntity changedTask, Long taskId) throws TaskNotFoundException,
            AccessTaskException {
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена!"));
        if (!task.isOwning()) {
            throw new AccessTaskException("Изменять можно только собственную задачу");
        }
        startPossibleChanges(task, changedTask);
        return Task.toModel(taskRepo.save(task));
    }

    public void startPossibleChanges(TaskEntity task, TaskEntity changedTask) {
        task.changeTime(changedTask, task);
        task.changeDescription(changedTask, task);
        task.changeAddress(changedTask, task);
        task.changeTitle(changedTask, task);
        task.changeParticipants(changedTask, task);
    }

    public Task receiveTask(Long taskId) throws TaskNotFoundException {
        TaskEntity task = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена!"));
        return Task.toModel(task);
    }

    public User getBetweenTimes(TimeEntity time, Long userId)
            throws UserNotFoundException, InvalidDataTimeException {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
        try {
            List<TaskEntity> tasks = user.getTasks();
            List<TaskEntity> updatedTask = tasks.stream()
                    .filter(entity -> {
                        try {
                            return isInRange(entity.getTime(), time);
                        } catch (InvalidDataTimeException e) {
                            throw new RuntimeException(e);
                        }
                    }).sorted((firstTask, secondTask)
                            -> {
                        try {
                            return firstMoreSecond(firstTask.getTime(), secondTask.getTime());
                        } catch (InvalidDataTimeException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
            user.setTasks(updatedTask);

        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDataTimeException("Неверный формат или несуществующее время");
        }

        return User.toModel(user);
    }

    public int firstMoreSecond(String firstTime, String secondTime) throws InvalidDataTimeException {
        try {
            final int conditionIsTrue = 1, conditionIsFalse = -1;

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            LocalDateTime first = LocalDateTime.parse(firstTime, dateFormat);
            LocalDateTime second = LocalDateTime.parse(secondTime, dateFormat);

            if (first.isAfter(second)) {
                return conditionIsTrue;
            } else {
                return conditionIsFalse;
            }
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDataTimeException("Неверный формат или несуществующее время");
        }
    }

    public boolean isInRange(String taskTime, TimeEntity time) throws InvalidDataTimeException {
        boolean firstCondition, secondCondition;
        firstCondition = firstMoreSecond(time.getEndTime(), taskTime) == 1;
        secondCondition = firstMoreSecond(taskTime, time.getStartTime()) == 1;
        return firstCondition && secondCondition;
    }

    public User getActual(Long userId) throws UserNotFoundException {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        List<TaskEntity> tasks = user.getTasks();
        List<TaskEntity> updatedTask =
                tasks.stream()
                        .filter(entity -> {
                            try {
                                return isActualTime(entity.getTime()) && (!entity.isCompleted());
                            } catch (InvalidDataTimeException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList());

        user.setTasks(updatedTask);
        return User.toModel(user);
    }

    public static boolean isActualTime(String current) throws InvalidDataTimeException {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            String caseStartDate = dateFormat.format(LocalDateTime.now());
            LocalDateTime start = LocalDateTime.parse(current, dateFormat);
            LocalDateTime stop = LocalDateTime.parse(caseStartDate, dateFormat);
            return start.isAfter(stop);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDataTimeException("Неверный формат или несуществующее время");
        }
    }
}