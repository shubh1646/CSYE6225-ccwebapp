package com.neu.webapp.services;

import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book getById(UUID id){
        Book book;
        try{
            book = bookRepository.findById(id).get();
        }catch(Exception exc) {
            book = null;
        }
        return book;
    }

    public void deleteById(UUID id){
        bookRepository.deleteById(id);
    }
}
