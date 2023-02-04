package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.EmptyEmailException;
import com.example.demo.exception.EmptyUsernameException;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody UserEntity user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok().body("Пользователь успешно сохранён!");
        } catch (UserAlreadyExistException | EmptyUsernameException | EmptyEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getUser(@RequestParam Long id) {
        try {
            return ResponseEntity.ok().body(userService.getOneUser(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok().body("Пользователь успешно удалён!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PutMapping("/changePassword/{id}")
    public ResponseEntity<Object> changePassword(@PathVariable Long id, @RequestParam String password) {
        try {
            return ResponseEntity.ok().body(userService.changeUserPassword(id, password));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}