package com.neu.webapp.services;

import com.neu.webapp.errors.BookAdditionStatus;
import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.restControllers.CoverRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverRestController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CoverService coverService;

    public List<Book> getAllBooks() {
        Iterable<Book> books = bookRepository.findAll();
        List<Book> list = new ArrayList<>();
        for(Book book: books) {
            Cover cover = book.getImage();
            Book tempBook = new Book(book);
            if(cover!=null) tempBook.setImage(coverService.getPresignedUrl(cover.getId()));
            list.add(tempBook);
        }
        return list;
    }

    public Book CreateBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    public void UpdateBook(Book book) {
        // get the book that need to be updated
        Book upBook = bookRepository.findById(book.getId()).get();

        //check for fields that need to be updated
        if (book.getAuthor() != null) upBook.setAuthor(book.getAuthor());
        if (book.getIsbn() != null)  upBook.setIsbn(book.getIsbn());
        if (book.getTitle() != null) upBook.setTitle(book.getTitle());
        if (book.getQuantity() != null) upBook.setQuantity(book.getQuantity());

        bookRepository.save(upBook);
    }

    public Book getBookById(UUID id) {
        Optional<Book> temp = bookRepository.findById(id);
        return (temp.isPresent()) ? temp.get() : null;
    }

    public Book getBook(UUID id){
        Book book = getBookById(id);
        if (book!=null) {
            Book temp = new Book(book);
            if(isBookImagePresent(book)) temp.setImage(coverService.getPresignedUrl(book.getImage().getId()));
            return temp;
        }
        return null;
    }

    public void deleteById(UUID id) {
        try{
            Book book = getBookById(id);
            if (isBookImagePresent(book)) coverService.deleteFile(book.getImage().getUrl());
            bookRepository.deleteById(id);
        }catch(Exception exc) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            LOGGER.error(exc.getMessage()+sw.toString());
        }
    }

    public BookAdditionStatus getStockingStatus(BindingResult errors) {
        FieldError titleError = errors.getFieldError("title");
        FieldError authorError = errors.getFieldError("author");
        FieldError isbnError = errors.getFieldError("isbn");
        FieldError quantityError = errors.getFieldError("quantity");

        String titleErrorMessage = titleError == null ? "-" : titleError.getCode();
        String authorErrorMessage = authorError == null ? "-" : authorError.getCode();
        String isbnErrorMessage = isbnError == null ? "-" : isbnError.getCode();
        String quantityErrorMessage = quantityError == null ? "-" : quantityError.getCode();

        return new BookAdditionStatus(titleErrorMessage, authorErrorMessage, isbnErrorMessage, quantityErrorMessage);
    }

    public boolean isBookImagePresent(Book book) {
        if(book.getImage() == null) return false;
        return true;
    }
}
