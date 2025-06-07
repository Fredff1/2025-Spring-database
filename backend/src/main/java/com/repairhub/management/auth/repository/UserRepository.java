package com.repairhub.management.auth.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.PageResponse;

public interface UserRepository {
    int insert(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(UserRole role);
    PageResponse<User> findAllByRoleWithPage(UserRole role,int pageNum,int pageSize);
    List<User> findAll();
    int updateStatus(Long userId, UserStatus status);
    int updateBasicInfo(Long userId, String username, String phone, String email);
    int deleteById(Long userId);

}
