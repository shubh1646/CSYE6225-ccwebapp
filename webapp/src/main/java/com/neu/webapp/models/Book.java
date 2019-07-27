package com.neu.webapp.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "book_id", columnDefinition = "BINARY(16)")
    private UUID id;

    private String title;

    private String author;

    private String isbn;

    private Short quantity;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Cover image;

    public Book() {
    }

//    public Book(Book book, Cover image) {
//        this.id = book.getId();
//        this.title = book.getTitle();
//        this.author = book.getAuthor();
//        this.isbn = book.getIsbn();
//        this.quantity = book.getQuantity();
//        this.image = image;
//    }

    public Book(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.quantity = book.getQuantity();
    }
}

