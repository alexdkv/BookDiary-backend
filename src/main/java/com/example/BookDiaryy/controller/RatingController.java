package com.example.BookDiaryy.controller;

import com.example.BookDiaryy.model.dto.RatingRequestDTO;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.UserRepository;
import com.example.BookDiaryy.service.RatingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/book/rating")
public class RatingController {
    private final RatingService ratingService;
    private final UserRepository userRepository;

    public RatingController(RatingService ratingService, UserRepository userRepository) {
        this.ratingService = ratingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> rateBook(@PathVariable Long bookId, @RequestBody RatingRequestDTO ratingRequest){
        try{
            User user = userRepository.findById(ratingRequest.getUserId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            ratingService.rateBook(bookId, user , ratingRequest.getStars());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (UsernameNotFoundException | IllegalArgumentException | EntityNotFoundException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Double> getAvgRating(@PathVariable Long bookId){
        return new ResponseEntity<Double>(ratingService.getAvgRating(bookId) ,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Integer> getUSerRating(@RequestParam Long bookId, @RequestParam Long userId){
        return ResponseEntity.ok(ratingService.getUserRating(bookId,userId));
    }
}
