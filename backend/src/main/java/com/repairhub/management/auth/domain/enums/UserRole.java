package com.repairhub.management.auth.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority{
    CUSTOMER,
    REPAIRMAN,
    ADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
