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

    public void create(Book book){
        bookRepository.save(book);
    }

    public Book getById(UUID id){
        Book book = bookRepository.findById(id).get();
        return book;
    }

    public Boolean findById(UUID id){
        if(bookRepository.findById(id).isPresent()) {
            return true;
        }
        else{
            return false;
        }
    }

    public void deleteById(UUID id){
        bookRepository.deleteById(id);
    }
}
