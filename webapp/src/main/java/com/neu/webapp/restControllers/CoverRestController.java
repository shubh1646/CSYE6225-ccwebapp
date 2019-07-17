package com.neu.webapp.restControllers;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.services.BookService;
import com.neu.webapp.services.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/bookshubham/{idBook}/image")
@Validated
public class CoverRestController {
    @Autowired
    private BookService bookService;

    @Autowired
    private CoverService coverService;

    @GetMapping("/{idImage}")
    public ResponseEntity<?> getBookCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");

        if(coverService.getCoverById(idImage) == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Image_ID\" }");

        Cover cover = coverService.getPresignedUrl(idImage);
        return ResponseEntity.status(HttpStatus.OK).body(cover);
    }

    @PostMapping
    public ResponseEntity<?> addCover(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile image, HttpServletRequest request) throws Exception{
        if(!coverService.isImagePresent(image)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Select a file\" }");
        if(!coverService.isFileFormatRight(image.getContentType())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Image File Format Wrong\" }");

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");
        if(bookService.isBookImagePresent(book)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Cover exist already perform PUT to modify\" }");

        String localPath = request.getServletContext().getRealPath("/images/");
        Cover cover = coverService.addCover(book, image, localPath);

        return ResponseEntity.status(HttpStatus.OK).body(cover);
    }

    @PutMapping("/{idImage}")
    public ResponseEntity<?> updateCover(@PathVariable UUID idBook, @PathVariable UUID idImage, @RequestParam(required = false) MultipartFile image, HttpServletRequest request) throws Exception{
        if(!coverService.isImagePresent(image)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Select a file\" }");
        if(!coverService.isFileFormatRight(image.getContentType())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Image File Format Wrong\" }");

        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");
        if(!bookService.isBookImagePresent(book)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Cover does not exist already perform POST to add cover\" }");

        Cover cover = coverService.getCoverById(idImage);
        if(cover == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Image_ID\" }");

        String localPath = request.getServletContext().getRealPath("/images/");
        coverService.updateCover(book, cover, image, localPath);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
        Book book = bookService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");
        if(!bookService.isBookImagePresent(book)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Cover does not exist already perform POST to add cover\" }");

        Cover cover = coverService.getCoverById(idImage);
        if(cover == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Image_ID\" }");

        coverService.deleteCover(book, cover);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }
}
