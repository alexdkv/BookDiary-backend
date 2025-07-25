package com.example.BookDiaryy.repository;

import com.example.BookDiaryy.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookById(Long id);

    List<Book> findByUserId(Long userId);
}
