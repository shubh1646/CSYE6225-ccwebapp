package com.neu.webapp.restControllers;

import com.neu.webapp.models.Book;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;


@RestController
public class BookRestController {

    public static final Logger logger = LoggerFactory.getLogger(BookRestController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;


    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookPerId( @PathVariable UUID id) {
            logger.info("Fetching User with id {}",id);
            bookRepository.findBookById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Id:" +id);
    }

    @DeleteMapping("/book/{id}")
    @Transactional
    public ResponseEntity<?> deleteBookById( @PathVariable("id") UUID id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("This is Deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO CONTENT");
        }
    }


    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public Book create(@Valid @RequestBody Book book) {
//        if(bookRepository.findById(book.getId()).isPresent()) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }

        bookService.create(book);
        return book;
//        Book savedStudent = bookRepository.save(book);
//return
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
//                .buildAndExpand(savedStudent.getId()).toUri();
//
//        return new ResponseEntity<>(location,HttpStatus.CREATED);

    }

}
