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
        // get the book that need to be updated
        Book upBook = bookRepository.findById(book.getId()).get();


        //check for fields that need to be updated
        if (book.getAuthor() != null) {
            upBook.setAuthor(book.getAuthor());
        }

        if (book.getIsbn() != null) {
            upBook.setIsbn(book.getIsbn());
        }

        if (book.getTitle() != null) {
            upBook.setTitle(book.getTitle());

        }

        if (book.getQuantity() != null) {
            upBook.setQuantity(book.getQuantity());
        }

        bookRepository.save(upBook);


    }


    public Book getById(UUID id)
        {
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
