package com.example.BookDiaryy.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;

@Entity
public class User extends BaseEntity implements Serializable {

    private String username;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Book> books;

    public User(List<Book> books, String email, String password, String username) {
        this.books = books;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
