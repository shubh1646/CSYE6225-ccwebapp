package com.neu.webapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "book_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "QUANTITY")
    private String quantity;

    private Book(){

    }

    public Book(UUID id, @NotBlank String title, @NotBlank String author, @NotBlank String isbn, @NotBlank String quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
