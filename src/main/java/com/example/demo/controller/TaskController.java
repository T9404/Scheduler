package com.example.demo.controller;

import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.TimeEntity;
import com.example.demo.exception.*;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Object> createTask(@RequestBody TaskEntity task, @RequestParam Long userId) {
        try {
            return ResponseEntity.ok().body(taskService.createTask(task, userId));
        } catch (UserNotFoundException | InvalidTaskIntervalException | OverflowTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping("/suggest/{username}/{taskId}")
    public ResponseEntity<Object> suggestTask(@PathVariable String username, @PathVariable Long taskId) {
        try {
            taskService.suggestTaskToUser(username, taskId);
            return ResponseEntity.ok().body("Задача успешна предложена!");
        } catch (UserNotFoundException | InvalidTaskIntervalException |
                 TaskNotFoundException | OverflowTaskException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PutMapping("/complete")
    public ResponseEntity<Object> completeTask(@RequestParam Long taskId) {
        try {
            return ResponseEntity.ok(taskService.completeTask(taskId));
        } catch (AccessTaskException | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteTask(@RequestParam Long taskId) {
        try {
            return ResponseEntity.ok(taskService.deleteTask(taskId));
        } catch (AccessTaskException | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<Object> changeTask(@RequestBody TaskEntity changedTask,
                                             @RequestParam Long taskId) {
        try {
            return ResponseEntity.ok(taskService.changeTask(changedTask, taskId));
        } catch (AccessTaskException | TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/getActual")
    public ResponseEntity<Object> getActualTasks(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok().body(taskService.getActual(userId).getTaskList());
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/getBetween/{userId}")
    public ResponseEntity<Object> getTasksBetweenTimes(@PathVariable Long userId,
                                                       @RequestBody TimeEntity time) {
        try {
            return ResponseEntity.ok().body(taskService.getBetweenTimes(time, userId).getTaskList());
        } catch (UserNotFoundException | InvalidDataTimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getTask(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(taskService.receiveTask(id));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}