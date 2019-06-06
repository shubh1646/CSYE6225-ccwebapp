package com.neu.webapp.restControllers;

import com.neu.webapp.models.Book;
import com.neu.webapp.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;


@RestController
public class BookRestController {

    public static final Logger logger = LoggerFactory.getLogger(BookRestController.class);

    @Autowired
    private BookService bookService;


    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookPerId( @PathVariable UUID id) {
        if(bookService.findById(id) == true){
            logger.info("Fetching User with id {}",id);
            Book book = bookService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }

    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBookById( @PathVariable("id") UUID id) {
        if (bookService.findById(id) == true) {
            bookService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This is Deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad Request");
        }
    }

}
