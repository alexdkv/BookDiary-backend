package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.entity.Role;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.UserRepository;
import com.example.BookDiaryy.service.auth.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

public class LoginDetailsService implements UserDetailsService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HttpServletResponse response;

    public LoginDetailsService(JwtService jwtService, UserRepository userRepository, HttpServletResponse response) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.response = response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        UserDetails userDetails = user.map(LoginDetailsService::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " does not exist."));
        Cookie cookie = new Cookie("jwt", jwtService.generateToken(userDetails));
        cookie.setMaxAge(60*60*24*1000*10);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return userDetails;
    }

    private static UserDetails map(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRole().stream().map(LoginDetailsService::map).collect(Collectors.toList())
        );
    }

    private static GrantedAuthority map(Role role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role.getName().name()
        );
    }
}
