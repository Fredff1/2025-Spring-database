package com.repairhub.management.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.repairhub.management.utils.JwtUtil;

@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Autowired
    private SecurityProperties securityProperties;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 密码加密方式
     * 
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProperties.getWhitelist().toArray(new String[0])).permitAll()
                        // 只有管理员可以访问的接口
                        .requestMatchers(securityProperties.getAdminAllowedList().toArray(new String[0]))
                        .hasRole("ADMIN")
                        // 普通用户和管理员都可以访问的接口
                        .requestMatchers(securityProperties.getUserAllowedList().toArray(new String[0]))
                        .hasAnyRole("USER", "ADMIN", "STAFF","CUSTOMER")
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
