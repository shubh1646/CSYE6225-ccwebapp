package com.neu.webapp.services;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
public class CoverService {
    @Autowired
    private BookRepository bookRepository;

    public Cover addCover(UUID idBook, MultipartFile imageFile) throws Exception{
        Book book = bookRepository.findById(idBook).get();
        String path = "/home/cyrilsebastian/Desktop/BookCovers"+imageFile.getOriginalFilename();
        if (book.getImage() == null){
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(imageFile.getBytes());
            fout.close();

            book.setImage(new Cover(path));
            bookRepository.save(book);
            return book.getImage();
        }else { return null; }
    }
}
