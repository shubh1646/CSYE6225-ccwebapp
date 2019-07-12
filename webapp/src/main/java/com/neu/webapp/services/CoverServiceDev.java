package com.neu.webapp.services;

import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.repositories.CoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("dev")
public class CoverServiceDev implements CoverService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CoverRepository coverRepository;

    public Cover getPresignedUrl(UUID id) {
        Cover cover = getCoverById(id);
        return cover;
    }

    public Cover getCoverById(UUID id) {
        Optional<Cover> temp = coverRepository.findById(id);
        return temp.isEmpty() ? null : temp.get();
    }

    public String writeFile(MultipartFile imageFile, UUID id, String localPath) throws Exception{
        File dir = new File(localPath);
        if(!dir.exists()) {
            dir.mkdir();
        }
        localPath +=id+"-"+imageFile.getOriginalFilename();
        File file = new File(localPath);
        file.createNewFile();
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(imageFile.getBytes());
        fout.close();
        return localPath;
    }

    public Cover addCover(Book book, MultipartFile imageFile, String localPath) throws Exception{
        String path = writeFile(imageFile, book.getId(), localPath);
        Cover cover = new Cover(path);
        book.setImage(cover);
        bookRepository.save(book);
//        return book.getImage();
        return getPresignedUrl(book.getImage().getId());
    }

    public void deleteFile(String fileName) throws Exception {
        File file = new File(fileName);
        file.delete();
    }

    public void updateCover(Book book, Cover cover, MultipartFile imageFile, String localPath) throws Exception{
        deleteFile(cover.getUrl());
        String path = writeFile(imageFile, book.getId(), localPath);
        cover.setUrl(path);
        coverRepository.save(cover);
    }

    public void deleteCover(Book book, Cover cover) throws Exception{
        deleteFile(cover.getUrl());
        book.setImage(null);
        coverRepository.delete(cover);
    }
}
