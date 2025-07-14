package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.BookRepository;
import com.example.BookDiaryy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Book addBook(Long userId, Book book){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not Found"));
        book.setUser(user);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book updateBook(Book book){
        return bookRepository.save(book);
    }

    public Book getBookById(Long id){
        return bookRepository.findBookById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}

