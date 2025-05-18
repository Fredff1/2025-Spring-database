package com.repairhub.management.auth.repository;

import java.util.Optional;

import com.repairhub.management.auth.entity.User;

public interface UserRepository {
    int insert(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    int updateStatus(Long userId, String status);

}
