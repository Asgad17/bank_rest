package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);
    User createUser(User user);
}