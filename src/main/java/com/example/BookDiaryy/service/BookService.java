package com.example.BookDiaryy.service;

import com.example.BookDiaryy.model.entity.Book;
import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.BookRepository;
import com.example.BookDiaryy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        book.setRatings(new ArrayList<>());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book updateBook(Long id, Book book){
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(()->new NoSuchElementException("Book not Found"));
        bookToUpdate.setName(book.getName());
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setDescription(book.getDescription());
        bookToUpdate.setPages(book.getPages());
        bookToUpdate.setPhotoUrl(book.getPhotoUrl());
        bookToUpdate.setStatus(book.getStatus());
        return bookRepository.save(bookToUpdate);
    }

    public Book getBookById(Long id){
        return bookRepository.findBookById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}

