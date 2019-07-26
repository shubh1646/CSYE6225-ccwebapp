package com.neu.webapp.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.repositories.CoverRepository;
import com.neu.webapp.restControllers.CoverRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

@Service
@Profile("prod")
public class CoverServiceProd implements CoverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoverRestController.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CoverRepository coverRepository;

    @Value("${bucket.name}")
    private String BUCKET_NAME;

    @Override
    public Cover getCoverById(UUID id) {
        Optional<Cover> temp = coverRepository.findById(id);
        return temp.isPresent() ? temp.get() : null;
    }

    public  static File multipartToFile(MultipartFile imageFile, String imageName) throws IllegalStateException, IOException {
        File file = new File(System.getProperty("java.io.tmpdir")+"/"+imageName);
        imageFile.transferTo(file);
        return file;
    }

    @Override
    public Cover getPresignedUrl(UUID id) {
        Cover cover = getCoverById(id);
        try {
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 2;
            expiration.setTime(expTimeMillis);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(BUCKET_NAME, cover.getUrl())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
            return new Cover(cover.getId(), url.toString());
        } catch(AmazonServiceException exc) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            LOGGER.error(exc.getMessage()+sw.toString());
        } catch(SdkClientException exc) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            LOGGER.error(exc.getMessage()+sw.toString());
        }
        return null;
    }

    @Override
    public String writeFile(MultipartFile imageFile, UUID id, String localPath) throws Exception {
        String fileName = id+"-"+imageFile.getOriginalFilename();
        File file = multipartToFile(imageFile, fileName);
        try {
            s3.putObject(BUCKET_NAME, fileName, file);
        } catch (AmazonServiceException exc) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            LOGGER.error(exc.getMessage()+sw.toString());
        }
        return fileName;
    }

    @Override
    public Cover addCover(Book book, MultipartFile imageFile, String localPath) throws Exception{
        String path = writeFile(imageFile, book.getId(), localPath);
        Cover cover = new Cover(path);
        book.setImage(cover);
        bookRepository.save(book);
        return getPresignedUrl(book.getImage().getId());
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3.deleteObject(BUCKET_NAME, fileName);
        }catch (AmazonServiceException exc) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            LOGGER.error(exc.getMessage()+sw.toString());
        }
    }

    @Override
    public void updateCover(Book book, Cover cover, MultipartFile imageFile, String localPath) throws Exception {
        deleteFile(cover.getUrl());
        String path = writeFile(imageFile, book.getId(), localPath);
        cover.setUrl(path);
        book.setImage(cover);
        bookRepository.save(book);
    }

    @Override
    public void deleteCover(Book book, Cover cover) throws Exception{
        deleteFile(cover.getUrl());
        book.setImage(null);
        bookRepository.save(book);
        coverRepository.deleteById(cover.getId());
    }
}
