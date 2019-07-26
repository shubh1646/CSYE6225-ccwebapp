package com.neu.webapp.restControllers;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.services.BookService;
import com.neu.webapp.services.CoverService;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/bookshubham/{idBook}/image")
@Validated
public class CoverRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverRestController.class);

    @Autowired
    private StatsDClient metricsClient;

    @Autowired
    private BookService bookService;
    @Autowired
    private StatsDClient statsDClient;

    @Autowired
    private CoverService coverService;

    @GetMapping("/{idImage}")
    public ResponseEntity<?> getBookCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
         metricsClient.incrementCounter("endpoint.idimage.http.get");
        Book book = bookService.getBookById(idBook);
        Cover cover = coverService.getCoverById(idImage);

        ResponseEntity<?> temp = GetDeleteCommon(idImage, idBook, book, cover);
        if(temp!=null) return temp;

        LOGGER.info("Cover fetched");
        return ResponseEntity.status(HttpStatus.OK).body(coverService.getPresignedUrl(idImage));
    }

    @PostMapping
    public ResponseEntity<?> addCover(@PathVariable UUID idBook, @RequestParam(required = false) MultipartFile image, HttpServletRequest request) throws Exception{
        metricsClient.incrementCounter("endpoint.idimage.http.post");
        if(!coverService.isImagePresent(image)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Select a file\" }");
        if(!coverService.isFileFormatRight(image.getContentType())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Image File Format Wrong\" }");

        Book book = bookService.getBookById(idBook);

        ResponseEntity<?> temp = PutPostCommon(idBook, image, book);
        if(temp!=null) return temp;

        if(bookService.isBookImagePresent(book)) {
            LOGGER.warn("POST->Cover exist already perform PUT to modify");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"POST->Cover exist already perform PUT to modify\" }");
        }

        LOGGER.info("Cover added");
        String localPath = request.getServletContext().getRealPath("/images/");
        Cover cover = coverService.addCover(book, image, localPath);
        return ResponseEntity.status(HttpStatus.OK).body(cover);
    }

    @PutMapping("/{idImage}")
    public ResponseEntity<?> updateCover(@PathVariable UUID idBook, @PathVariable UUID idImage, @RequestParam(required = false) MultipartFile image, HttpServletRequest request) throws Exception{
        metricsClient.incrementCounter("endpoint./book/{idBook}/image/{idImage}.http.put");
        Book book = bookService.getBookById(idBook);

        ResponseEntity<?> temp = PutPostCommon(idBook, image, book);
        if(temp!=null) return temp;

        if(!bookService.isBookImagePresent(book)) {
            LOGGER.warn("PUT->Cover does not exist already perform POST to add cover");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"PUT->Cover does not exist already perform POST to add cover\" }");
        }

        Cover cover = coverService.getCoverById(idImage);
        if(cover == null) {
            LOGGER.warn("Cover by "+idImage+"does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Image_ID\" }");
        }

        LOGGER.info("Cover modified");
        String localPath = request.getServletContext().getRealPath("/images/");
        coverService.updateCover(book, cover, image, localPath);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }

    @DeleteMapping("/{idImage}")
    public ResponseEntity<?> deleteCover(@PathVariable UUID idBook, @PathVariable UUID idImage) throws Exception{
      metricsClient.incrementCounter("endpoint.idimage.http.delete");
        Book book = bookService.getBookById(idBook);
        Cover cover = coverService.getCoverById(idImage);

        ResponseEntity<?> temp = GetDeleteCommon(idImage, idBook, book, cover);
        if(temp!=null) return temp;

        LOGGER.info("Cover deleted");
        if(bookService.isBookImagePresent(book)) coverService.deleteCover(book, cover);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(cover);
    }


    public ResponseEntity<?> PutPostCommon(UUID idBook, MultipartFile image, Book book){
        if(!coverService.isImagePresent(image)) {
            LOGGER.warn("Image not received ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Select a file\" }");
        }
        if(!coverService.isFileFormatRight(image.getContentType())) {
            LOGGER.warn("Wrong image format");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{ \"error\": \"Image File Format Wrong\" }");
        }
        if(book == null) {
            LOGGER.warn("Book by "+idBook.toString()+" does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");
        }
        return null;
    }

    public ResponseEntity<?> GetDeleteCommon(UUID idImage, UUID idBook, Book book, Cover cover){
        if(book == null) {
            LOGGER.warn("Book by "+idBook.toString()+" does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Book_ID\" }");
        }

        if(cover == null) {
            LOGGER.warn("Cover by "+idImage.toString()+" does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{ \"error\": \"Wrong Image_ID\" }");
        }
        return null;
    }
}
