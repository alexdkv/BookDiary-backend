package com.example.BookDiaryy.controller;

import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

}
