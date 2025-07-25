package com.example.BookDiaryy.controller;

import com.example.BookDiaryy.model.dto.UserRegistrationDTO;
import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        System.out.println(registrationDTO.getUsername());
        System.out.println(registrationDTO.getEmail());
        System.out.println(registrationDTO.getPassword());
        System.out.println(registrationDTO.getConfirmPassword());
        boolean hasSuccessfulRegistration = userService.registerUser(registrationDTO);
        if (!hasSuccessfulRegistration){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed!");
        }
        return ResponseEntity.ok("Registration successful");
    }
}
