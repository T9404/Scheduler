package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public void registration(UserEntity user)
            throws UserAlreadyExistException, EmptyUsernameException, EmptyEmailException {
        if ((user.getUsername() == null) || (user.getUsername().length() == 0)) {
            throw new EmptyUsernameException("Поле username не должно быть пустым!");
        }
        if ((user.getEmail() == null) || (user.getEmail().length() == 0)) {
            throw new EmptyEmailException("Поле email не должно быть пустым!");
        }
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistException("Пользователь с такой почтой уже существует!");
        }
        userRepo.save(user);
    }

    public User getOneUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
        return User.toModel(user);
    }

    public void delete(Long id) throws UserNotFoundException {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
        userRepo.deleteById(user.getId());
    }

    public User changeUserPassword(Long id, String password) throws UserNotFoundException {
        UserEntity user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));
        user.setPassword(password);
        return User.toModel(userRepo.save(user));
    }
}