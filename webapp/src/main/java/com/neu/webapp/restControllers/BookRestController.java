package com.neu.webapp.restControllers;

import com.neu.webapp.errors.BookAdditionStatus;
import com.neu.webapp.models.Book;
import com.neu.webapp.services.BookService;
import com.neu.webapp.services.CoverService;
import com.neu.webapp.validators.BookValidator;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookRestController {
    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);

    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookValidator bookValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(bookValidator);
    }


    // "Post request to create books ";
    @PostMapping
    public ResponseEntity<?> createBooks(@Valid @RequestBody Book book, BindingResult errors) throws Exception{
        logger.info("Info Log");
        BookAdditionStatus bookAdditionStatus;
        statsDClient.incrementCounter("endpoint.book.http.post");
        if (errors.hasErrors()) {
            bookAdditionStatus = bookService.getStockingStatus(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookAdditionStatus);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.CreateBook(book));
    }

    //   "get request to return all the books ";
    @GetMapping
    public Iterable<Book> getAllBooks() throws Exception{
        logger.warn("Warn Log");
        statsDClient.incrementCounter("endpoint.book.http.get");
        Iterable<Book> allBooks = bookService.getAllBooks();
        return allBooks;

    }


    //PUT request to update all the books
    @PutMapping
    public ResponseEntity<?> updateBooks(@RequestBody Book book) throws Exception{
        //check id in json incomming payload
        statsDClient.incrementCounter("endpoint.book.http.put");
        if(book.getId() == null || book == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Book does not hae an ID\" }");
        }
        else{
            logger.error("Error updating all the books");
        }
        bookService.UpdateBook(book);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);    /// return return code according to the condition (custpm )
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getBookPerId( @PathVariable UUID id) throws Exception{
        statsDClient.incrementCounter("endpoint.id.http.get");
        Book book = bookService.getBookById(id);
        if(book != null){
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Not Found\" }");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById( @PathVariable("id") UUID id) throws Exception{
        statsDClient.incrementCounter("endpoint.id.http.delete");
        if (bookService.getBookById(id) != null) {
            bookService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Bad Request\" }");
        }
    }
}