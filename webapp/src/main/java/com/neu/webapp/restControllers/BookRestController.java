package com.neu.webapp.restControllers;


import com.neu.webapp.models.Book;
import com.neu.webapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
public class BookRestController {

    @Autowired
    private BookService bookService;


    // "Post request to create books ";
    @PostMapping("/book")
    public ResponseEntity createBooks(@RequestBody Book book) {
        return bookService.CreateBook(book);

    }


    //   "get request to return all the books ";
    @GetMapping("/book")
    public Iterable<Book> getAllBooks() {
        Iterable<Book> allBooks = bookService.getAllBooks();
        return allBooks;

    }


    //PUT request to update all the books
    @PutMapping("/book")
    public ResponseEntity updateBooks(@RequestBody Book book) {

        //check id in json incomming payload
        if(book.getId() == null || book == null )
        {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        bookService.UpdateBook(book);
        return new ResponseEntity(HttpStatus.ACCEPTED);     /// return return code according to the condition (custpm )
    }



    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookPerId( @PathVariable UUID id) {
        if(bookService.getById(id) != null){

            Book book = bookService.getById(id);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
        }

    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBookById( @PathVariable("id") UUID id) {
        if (bookService.getById(id) != null) {
            bookService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("This is Deleted");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bad Request");
        }
    }

}
