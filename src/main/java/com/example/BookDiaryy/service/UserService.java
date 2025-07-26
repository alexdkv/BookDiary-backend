package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.dto.UserRegistrationDTO;
import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.Role;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.model.enums.UserRoleENUM;
import com.example.BookDiaryy.repository.BookRepository;
import com.example.BookDiaryy.repository.RoleRepository;
import com.example.BookDiaryy.repository.UserRepository;
import com.example.BookDiaryy.service.auth.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public UserService(UserRepository userRepository, BookRepository bookRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public boolean registerUser(UserRegistrationDTO userRegistrationDTO){
        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())){
            return false;
        }
        boolean existsByUsernameOrEmail = userRepository.existsByUsernameOrEmail(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getEmail()
        );
        if (existsByUsernameOrEmail){
            return false;
        }
        userRepository.save(map(userRegistrationDTO));
        return true;
    }

    private User map(UserRegistrationDTO userRegistrationDTO){
        User mappedEntity = modelMapper.map(userRegistrationDTO, User.class);
        mappedEntity.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        Role role = roleRepository.findByName(UserRoleENUM.USER)
                .orElseThrow(() -> new IllegalArgumentException("Role USER not found in DB"));

        mappedEntity.setRole(Collections.singletonList(role));

        return mappedEntity;
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Book> getAllBooksByUserId(Long userId){
        return bookRepository.findByUserId(userId);
    }

}
