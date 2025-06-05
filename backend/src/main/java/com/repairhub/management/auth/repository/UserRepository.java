package com.repairhub.management.auth.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.entity.User;

public interface UserRepository {
    int insert(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(UserRole role);
    List<User> findAll();
    int updateStatus(Long userId, String status);
    int updateBasicInfo(Long userId, String username, String phone, String email);
}
