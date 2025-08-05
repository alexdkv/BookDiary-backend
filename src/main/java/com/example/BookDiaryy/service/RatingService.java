package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.Rating;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.BookRepository;
import com.example.BookDiaryy.repository.RatingRepository;
import com.example.BookDiaryy.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void rateBook(Long bookId, User user, int stars){
        if (stars < 1 || stars > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Book book = bookRepository.findBookById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        Rating rating = ratingRepository.findByBookIdAndUserId(bookId, user.getId()).orElse(new Rating());

        rating.setBook(book);
        rating.setUser(user);
        rating.setStars(stars);

        ratingRepository.save(rating);
    }

    public double getAvgRating(Long bookId){
        List<Rating> ratings = ratingRepository.findByBookId(bookId);
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }

    public int getUserRating(Long bookId, Long userId){
        return ratingRepository.findStarsByBookIdAndUserId(bookId, userId).orElse(0);
    }
}
