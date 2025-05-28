package com.repairhub.management.init;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.auth.entity.RepairmanProfile;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.RepairmanProfileRepository;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.RepairType;

@Component
public class DataInitializer implements ApplicationRunner{
    private final UserRepository userRepository;
    private final RepairmanProfileRepository repairmanProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository, 
        RepairmanProfileRepository repairmanProfileRepository,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.repairmanProfileRepository = repairmanProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User customer = User.builder()
            .username("alice")
            .password(passwordEncoder.encode("alice123"))
            .email("alice@example.com")
            .phone("123-456-7890")
            .role(UserRole.CUSTOMER)
            .status(UserStatus.ACTIVE)
            .build();
        userRepository.insert(customer);

        User repairman = User.builder()
            .username("bob")
            .password(passwordEncoder.encode("bob123"))
            .email("bob@repairhub.com")
            .phone("234-567-8901")
            .role(UserRole.REPAIRMAN)
            .status(UserStatus.ACTIVE)
            .build();
        userRepository.insert(repairman);
        RepairmanProfile repairmanProfile = RepairmanProfile.builder()
            .userId(repairman.getUserId())
            .specialty(FaultType.MAINTENANCE)
            .hourlyMoneyRate(new BigDecimal("100.00"))
            .build();
        repairmanProfileRepository.insert(repairmanProfile);
        
        User admin = User.builder()
            .username("carol")
            .password(passwordEncoder.encode("carol123"))
            .email("carol@repairhub.com")
            .phone("345-678-9012")
            .role(UserRole.ADMIN)
            .status(UserStatus.ACTIVE)
            .build();
        
        userRepository.insert(admin);
    }
}
