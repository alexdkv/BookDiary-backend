package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(UserDetailsService::map)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private static UserDetails map(User user){

        //GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().getFirst().getName());

        List<GrantedAuthority> authorities = user.getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(false)
                .build();

    }
}
