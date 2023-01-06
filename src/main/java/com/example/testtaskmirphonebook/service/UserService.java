package com.example.testtaskmirphonebook.service;

import com.example.testtaskmirphonebook.entity.UserEntity;
import com.example.testtaskmirphonebook.exception.IllegalStrategyException;
import com.example.testtaskmirphonebook.exception.UserNotFoundException;
import com.example.testtaskmirphonebook.exception.UserNotUniqueException;
import com.example.testtaskmirphonebook.model.User;
import com.example.testtaskmirphonebook.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserEntity addUser(UserEntity user) throws UserNotUniqueException {
        if (userRepo.findByName(user.getName()).orElse(null) != null) {
            throw new UserNotUniqueException("Такой пользователь уже есть в справочнике");
        }
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll().stream()
                .map(User::toModel).collect(Collectors.toList());
    }

    public List<User> getAllUsersSorted(String sortBy) throws IllegalStrategyException {
        List<String> sortStrategies = List.of("name", "dateOfBirth");
        if (!sortStrategies.contains(sortBy)) {
            throw new IllegalStrategyException("Стратегия сортировки не распознана");
        }
        return userRepo.findAll(Sort.by(Sort.Direction.ASC, sortBy)).stream()
                .map(User::toModel).collect(Collectors.toList());
    }

    public User getUser(String name) throws UserNotFoundException {
        UserEntity entity = userRepo.findByName(name).orElse(null);
        if (entity == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(entity);
    }

    public User updateUsername(String name, String newName) throws UserNotUniqueException, UserNotFoundException {
        UserEntity entity = userRepo.findByName(name).orElse(null);
        if (entity == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (userRepo.findByName(newName).orElse(null) != null) {
            throw new UserNotUniqueException("Пользователь с таким именем уже существует");
        }
        entity.setName(newName);
        return User.toModel(entity);
    }

    public User deleteUser(UUID id) throws UserNotFoundException {
        UserEntity entity = userRepo.findById(id).orElse(null);
        if (entity == null) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        userRepo.deleteById(id);
        return User.toModel(entity);
    }
}
