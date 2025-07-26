package com.example.BookDiaryy.controller;

import com.example.BookDiaryy.model.dto.LoginDTO;
import com.example.BookDiaryy.model.dto.UserRegistrationDTO;
import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.service.UserService;
import com.example.BookDiaryy.service.auth.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/add")
    public ResponseEntity<User> adduser(@RequestBody User user){
        User newUser = userService.addUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id")Long id){
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getAllBooksByUser(@PathVariable("id")Long id){
        List<Book> books = userService.getAllBooksByUserId(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDTO registrationDTO){
        boolean hasSuccessfulRegistration = userService.registerUser(registrationDTO);
        if (!hasSuccessfulRegistration){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed!");
        }
        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(), loginDTO.getPassword()
                    )
            );
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return  ResponseEntity.ok((Map.of("token", token)));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username and password");
        }
    }
}
