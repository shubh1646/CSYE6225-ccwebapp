package com.neu.webapp.restControllers;

import com.neu.webapp.errors.BookAdditionStatus;
import com.neu.webapp.models.Book;
import com.neu.webapp.services.BookService;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(BookRestController.class);

    @Autowired
    private StatsDClient metricsClient;

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
       metricsClient.incrementCounter("endpoint.book.http.post");
        BookAdditionStatus bookAdditionStatus;
        if (errors.hasErrors()) {
            LOGGER.warn("Book validation failed");
            bookAdditionStatus = bookService.getStockingStatus(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bookAdditionStatus);
        }
        LOGGER.info("Book added");
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.CreateBook(book));
    }

    //   "get request to return all the books ";
    @GetMapping
    public Iterable<Book> getAllBooks() throws Exception{
         metricsClient.incrementCounter("endpoint.book.http.get");
        Iterable<Book> allBooks = bookService.getAllBooks();
        LOGGER.info("All Books fetched");
        return allBooks;
    }


    //PUT request to update all the books
    @PutMapping
    public ResponseEntity<?> updateBooks(@RequestBody Book book) {
        metricsClient.incrementCounter("endpoint./book.http.put");
        if(book == null || book.getId() == null) {
            LOGGER.warn("Book does not have an ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Book does not have an ID\" }");
        }
        LOGGER.info("Book modified");
        bookService.UpdateBook(book);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);    /// return return code according to the condition (custpm )
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookPerId( @PathVariable UUID id) throws Exception{
        Book book = bookService.getBookById(id);
         metricsClient.incrementCounter("endpoint.book.id.http.get");
        if(book != null){
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Not Found\" }");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookById( @PathVariable("id") UUID id) throws Exception{
        metricsClient.incrementCounter("endpoint./book/{id}.http.delete");
        if (bookService.getBookById(id) == null) {
            LOGGER.warn("No book with id: "+id.toString()+" present");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Bad Request\" }");
        }
        LOGGER.info("Book deleted");
        bookService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}