package com.neu.webapp.restControllers;

import com.neu.webapp.models.Cover;
import com.neu.webapp.services.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class CoverRestController {

    @Autowired
    private CoverService coverService;

    @PostMapping(value = "/{idBook}/image", headers = {"Accept=image/jpeg,image/png"})
    public ResponseEntity<?> addCover(@PathVariable UUID idBook, @RequestParam MultipartFile imageFile) throws Exception{
        Cover cover = coverService.addCover(idBook, imageFile);
        if (cover != null ) return ResponseEntity.status(HttpStatus.OK).body(cover);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cover already exists to update send PUT request");
    }

//    @GetMapping("/{idBook}/image/{idImage}")
//    public ResponseEntity<?> updateCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
//
//    }
}
