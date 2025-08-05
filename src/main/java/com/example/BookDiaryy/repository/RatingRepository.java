package com.example.BookDiaryy.repository;

import com.example.BookDiaryy.model.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBookId(Long bookId);
    Optional<Rating> findByBookIdAndUserId(Long bookId, Long userId);
    @Query("SELECT r.stars FROM Rating r WHERE r.book.id = :bookId AND r.user.id = :userId")
    Optional<Integer> findStarsByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
}
