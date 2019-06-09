package com.neu.webapp.restControllers;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.services.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/book")
@Validated
public class CoverRestController {

    @Autowired
    private CoverService coverService;

    @PostMapping("/{idBook}/image")
    public ResponseEntity<?> addCover(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile imageFile) throws Exception{
        if(imageFile == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Select a file");
        if(!coverService.fileFormatCheck(imageFile.getContentType())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image File Format Wrong");

        Book book = coverService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Book_ID");
        if(book.getImage() != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover exist already perform PUT to modify");

        Cover cover = coverService.addCover(book, imageFile);

        return ResponseEntity.status(HttpStatus.OK).body(cover);
    }

    @PutMapping("/{idBook}/image/{idImage}")
    public ResponseEntity<?> updateCover(@PathVariable UUID idBook, @PathVariable UUID idImage, @RequestParam(required = false) MultipartFile imageFile) throws Exception{
        if(imageFile == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Select a file");
        if(!coverService.fileFormatCheck(imageFile.getContentType())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image File Format Wrong");

        Book book = coverService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Book_ID");
        if(book.getImage() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover does not exist already perform POST to add cover");

        Cover cover = coverService.getCoverById(idImage);
        if(cover == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Image_ID");
        coverService.updateCover(book, cover, imageFile);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }

    @DeleteMapping("/{idBook}/image/{idImage}")
    public ResponseEntity<?> deleteCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
        Book book = coverService.getBookById(idBook);
        if(book == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Book_ID");
        if(book.getImage() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover does not exist already perform POST to add cover");

        Cover cover = coverService.getCoverById(idImage);
        if(cover == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong Image_ID");
        coverService.deleteCover(book, cover);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }
}
