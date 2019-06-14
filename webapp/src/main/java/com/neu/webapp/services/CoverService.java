package com.neu.webapp.services;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.repositories.CoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoverService {
    private static String path = System.getProperty("user.home")+"/Desktop/BookCoverImages";
    private static final String JPEG = "image/jpeg";
    private static final String JPG = "image/jpg";
    private static final String PNG = "image/png";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CoverRepository coverRepository;

    public boolean isImagePresent(MultipartFile imageFile) {
        if(imageFile == null) return false;
        return true;
    }

    public boolean isFileFormatRight(String fileMimeType) {
        if(fileMimeType.equals(JPEG) || fileMimeType.equals(JPG) || fileMimeType.equals(PNG)) return true;
        return false;
    }

    public Cover getCoverById(UUID id) {
        Optional<Cover> temp = coverRepository.findById(id);
        return temp.isEmpty() ? null : temp.get();
    }

    public String writeFile(MultipartFile imageFile, UUID id) throws Exception{
        File dir = new File(this.path);
        if(!dir.exists()) {
            dir.mkdir();
        }
        String path = this.path+"/"+id+"-"+imageFile.getOriginalFilename();
        File file = new File(path);
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(imageFile.getBytes());
        fout.close();
        return path;
    }

    public void deleteFile(String oldPath) throws Exception{
        File file = new File(oldPath);
        file.delete();
    }

    public Cover addCover(Book book, MultipartFile imageFile) throws Exception{
        String path = writeFile(imageFile, book.getId());
        book.setImage(new Cover(path));
        bookRepository.save(book);
        return book.getImage();
    }

    public void updateCover(Book book, Cover cover, MultipartFile imageFile) throws Exception{
        deleteFile(cover.getUrl());
        String path = writeFile(imageFile, book.getId());
        cover.setUrl(path);
        coverRepository.save(cover);
    }

    public void deleteCover(Book book, Cover cover) throws Exception{
        deleteFile(cover.getUrl());
        book.setImage(null);
        coverRepository.delete(cover);
    }
}
