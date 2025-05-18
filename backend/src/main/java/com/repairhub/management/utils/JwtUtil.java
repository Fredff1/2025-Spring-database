package com.repairhub.management.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-time}")
    private long expirationTime; // 过期时间（毫秒）

    private final Map<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成 JWT Token
     *
     * @param username 用户名作为主题
     * @return JWT 字符串
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证并解析 JWT Token
     *
     * @param token JWT 字符串
     * @return Claims 对象，包含 Token 中的声明信息
     */
    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取 Token 过期时间
     *
     * @param token JWT 字符串
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getExpiration();
    }

    /**
     * 从 JWT Token 中获取用户名（即 subject）
     *
     * @param token JWT 字符串
     * @return token 中的 subject，即用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    /**
     * 判断 JWT Token是否有效
     * 
     * @param token
     * @return jwt是否有效的布尔值
     */
    public boolean isValidToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            validateToken(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 让 Token 失效（加入黑名单）
     * @param token 需要失效的目标的token
     */
    public void addToBlacklist(String token) {
        long expiryTimestamp = this.getExpirationFromToken(token).getTime();
        tokenBlacklist.put(token, expiryTimestamp);
    }

    /**
     * 检查 Token 是否已失效（在黑名单中）
     * @param token 目标检查的token
     * @return token是否在黑名单
     */
    public boolean isTokenBlacklisted(String token) {
        Long expiry = tokenBlacklist.get(token);
        if (expiry == null) {
            return false;
        }
        // 如果 Token 过期，则自动删除
        if (System.currentTimeMillis() > expiry) {
            tokenBlacklist.remove(token);
            return false;
        }
        return true;
    }
}
