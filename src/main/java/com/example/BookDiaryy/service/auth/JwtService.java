package com.example.BookDiaryy.service.auth;

import com.example.BookDiaryy.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails user);
    String getUsernameFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    String generateTokenFromUsername(String username);
}
