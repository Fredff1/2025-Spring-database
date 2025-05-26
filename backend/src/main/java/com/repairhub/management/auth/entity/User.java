package com.repairhub.management.auth.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.repairhub.management.auth.domain.enums.UserRole ;
import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.vehicle.entity.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    private Long    userId;
    private String  username;
    private UserRole role;
    private String  password;
    private String  email;
    private String  phone;
    private UserStatus  status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Vehicle> vehicles;
    private List<RepairOrder> repairOrders;
    private List<RepairRecord> repairRecords;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}
