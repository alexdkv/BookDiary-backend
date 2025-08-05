package com.example.BookDiaryy.model.entity;

import com.example.BookDiaryy.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Book extends BaseEntity implements Serializable {
    private String name;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int pages;

    private String photoUrl;

    @Enumerated
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JsonBackReference
    @JsonIgnoreProperties({"books"})
    private User user;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Rating> ratings;

    public Book(String author, String description, String name, int pages, String photoUrl, Status status, User user) {
        this.author = author;
        this.description = description;
        this.name = name;
        this.pages = pages;
        this.photoUrl = photoUrl;
        this.status = status;
        this.user = user;
        this.ratings = new ArrayList<>();
    }



    public Book(){
        this.ratings  = new ArrayList<>();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}

