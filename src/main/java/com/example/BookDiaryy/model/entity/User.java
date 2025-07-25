package com.example.BookDiaryy.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseEntity implements Serializable {

    private String username;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Book> books;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> role;

    public User(String email, String password, String username, List<Book> books) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.books = books;
    }

    public User() {
        this.books = new ArrayList<>();
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

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        this.role = role;
    }
}
