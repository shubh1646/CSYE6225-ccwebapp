package com.neu.webapp.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CoverService {
    String JPEG = "image/jpeg";
    String JPG = "image/jpg";
    String PNG = "image/png";

    AmazonS3 s3 = AmazonS3ClientBuilder.standard()
            .build();

    default boolean isImagePresent(MultipartFile imageFile) {
        if(imageFile == null) return false;
        return true;
    }

    default boolean isFileFormatRight(String fileMimeType) {
        if(fileMimeType.equals(JPEG) || fileMimeType.equals(JPG) || fileMimeType.equals(PNG)) return true;
        return false;
    }

    Cover getPresignedUrl(UUID id);

    Cover getCoverById(UUID id);

    String writeFile(MultipartFile imageFile, UUID id, String localPath) throws Exception;

    Cover addCover(Book book, MultipartFile imageFile, String localPath) throws Exception;

    void deleteFile(String fileName) throws Exception;

    void updateCover(Book book, Cover cover, MultipartFile imageFile, String localPath) throws Exception;

    void deleteCover(Book book, Cover cover) throws Exception;
}
