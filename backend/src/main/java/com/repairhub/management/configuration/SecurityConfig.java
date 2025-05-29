package com.repairhub.management.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.utils.JwtUtil;

@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SecurityProperties securityProperties;

    public SecurityConfig(
        JwtUtil jwtUtil,
        JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtUtil = jwtUtil;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:5173")); // 或者 List.of("*")
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization"));        // 如果你要让前端读到某些响应头
        cfg.setAllowCredentials(true);                         // 如果前端要发送 Cookie
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS).permitAll()
            .requestMatchers(securityProperties.getWhitelist().toArray(new String[0])).permitAll()
            // 只有管理员可以访问的接口
            .requestMatchers(securityProperties.getAdminAllowedList().toArray(new String[0]))
            .hasRole(UserRole.ADMIN.name())
            // 普通用户和管理员都可以访问的接口
            .requestMatchers(securityProperties.getUserAllowedList().toArray(new String[0]))
            .hasAnyRole(UserRole.ADMIN.name(), UserRole.REPAIRMAN.name(), UserRole.CUSTOMER.name())
            .requestMatchers(securityProperties.getRepairmanAllowedList().toArray(new String[0]))
            .hasAnyRole(UserRole.ADMIN.name(), UserRole.REPAIRMAN.name())
            .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
