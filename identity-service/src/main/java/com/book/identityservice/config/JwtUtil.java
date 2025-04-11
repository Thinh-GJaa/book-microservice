package com.book.identityservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "mySecretKeymySecretKeymySecretKeymySecretKey"; // Ít nhất 32 bytes

    // 🔹 Lấy khóa ký từ SECRET_KEY
    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(); // ⚠ Không cần BASE64 decode
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 🔹 Tạo token với userId và quyền duy nhất
    public String generateToken(UUID userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role) // Chỉ lưu 1 quyền duy nhất
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000*24*7)) // Hết hạn sau 7 ngày
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Lấy userId từ token
    public UUID extractUserId(String token) {
        return UUID.fromString(extractClaims(token).getSubject()) ;
    }

    // 🔹 Lấy quyền từ token
    public String extractRole(String token) {
        System.out.println("Thinh; "+ extractClaims(token).get("role", String.class));
        return extractClaims(token).get("role", String.class);

    }

    // 🔹 Trích xuất claims từ token
    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("The token is invalid or has expired!");
        }
    }

    public UUID extractUserId(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    try {
                        // Giải mã token để lấy userId
                        return extractUserId(token);
                    } catch (Exception e) {
                        System.out.println("Invalid token: " + e.getMessage());
                        return null;
                    }
                }
            }
        }
        return null;
    }


}
