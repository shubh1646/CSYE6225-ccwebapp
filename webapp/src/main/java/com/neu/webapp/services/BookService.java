package com.neu.webapp.services;


import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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


    public Book CreateBook(Book book)
    {
        Book b = bookRepository.save(book);
            return book;
    }




    public void UpdateBook(Book book) {

        Book upBook = bookRepository.findById(book.getId()).get();
        if(book.getAuthor() != null)
        {
            upBook.setAuthor(book.getAuthor());
        }

        if(book.getIsbn() != null)
        {
            upBook.setIsbn(book.getIsbn());
        }

        if(book.getTitle() != null)
        {
            upBook.setTitle(book.getTitle());

        }

        if(book.getQuantity() != null)
        {
            upBook.setQuantity(book.getQuantity());
        }

        bookRepository.save(upBook);




    }
}
