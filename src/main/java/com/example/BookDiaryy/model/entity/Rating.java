package com.example.BookDiaryy.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Rating extends BaseEntity{

    @Min(1)
    @Max(5)
    private int stars;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Min(1)
    @Max(5)
    public int getStars() {
        return stars;
    }

    public void setStars(@Min(1) @Max(5) int stars) {
        this.stars = stars;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
