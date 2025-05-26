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
    int updateStatus(Long userId, String status);

}
