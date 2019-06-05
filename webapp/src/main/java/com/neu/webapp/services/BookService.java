package com.neu.webapp.services;


import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {


    @Autowired
    private BookRepository bookRepository;


    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    public void CreateBook(Book book) {
        Book b = bookRepository.save(book);

    }




    public void UpdateBook(Book book) {



        bookRepository.save(book);




    }
}
