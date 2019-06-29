package com.neu.webapp.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.neu.webapp.models.Book;
import com.neu.webapp.models.Cover;
import com.neu.webapp.repositories.BookRepository;
import com.neu.webapp.repositories.CoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import java.net.URL;

@Service
public class CoverService {
    private static final String JPEG = "image/jpeg";
    private static final String JPG = "image/jpg";
    private static final String PNG = "image/png";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CoverRepository coverRepository;

    private static AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                .withCredentials(new InstanceProfileCredentialsProvider(false))
                .build();

    @Value("${bucket.name}")
    private String BUCKET_NAME;

    @Value("${spring.profiles.active}")
    private String activeProfile;

//    @Profile("prod")
//    @Bean
//    private AmazonS3 setS3forProd() {
//        s3 = AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.US_EAST_1)
//                .build();
//        List<Bucket> buckets = s3.listBuckets();
//        for(Bucket bucket : buckets) {
//            String bucketName =bucket.getName();
//            if (bucketName.matches("^csye[a-z0-9A-Z\\.\\-]*.com$")) BUCKET_NAME = bucketName;
//        }
//        return s3;
//    }
//
//    @Profile("dev")
//    @Bean
//    private AmazonS3 setS3forDev() {
//        s3 = AmazonS3ClientBuilder.defaultClient();
//        return s3;
//    }

    public boolean isImagePresent(MultipartFile imageFile) {
        if(imageFile == null) return false;
        return true;
    }

    public boolean isFileFormatRight(String fileMimeType) {
        if(fileMimeType.equals(JPEG) || fileMimeType.equals(JPG) || fileMimeType.equals(PNG)) return true;
        return false;
    }

    public Cover getPresignedUrl(UUID id) {
        System.out.println(this.activeProfile+"------"+BUCKET_NAME);
        Cover cover = getCoverById(id);
        if(activeProfile.equals("prod")) {
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
            } catch(AmazonServiceException e) {
                e.printStackTrace();
            } catch(SdkClientException e) {
                e.printStackTrace();
            }
            return null;
        }else {
            return cover;
        }

    }

    public Cover getCoverById(UUID id) {
        Optional<Cover> temp = coverRepository.findById(id);
        return temp.isEmpty() ? null : temp.get();
    }

    public  static File multipartToFile(MultipartFile imageFile, String imageName) throws IllegalStateException, IOException {
        File file = new File(System.getProperty("java.io.tmpdir")+"/"+imageName);
        imageFile.transferTo(file);
        return file;
    }

    public String writeFile(MultipartFile imageFile, UUID id, String localPath) throws Exception{
        if(activeProfile.equals("prod")) {
            String fileName = id+"-"+imageFile.getOriginalFilename();
            File file = multipartToFile(imageFile, fileName);
            try {
                s3.putObject(BUCKET_NAME, fileName, file);
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
            return fileName;
        }else {
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
    }

    public Cover addCover(Book book, MultipartFile imageFile, String localPath) throws Exception{
        String path = writeFile(imageFile, book.getId(), localPath);
        Cover cover = new Cover(path);
        book.setImage(cover);
        bookRepository.save(book);
        return book.getImage();
    }

    public void deleteFile(String fileName) throws Exception {
        if(activeProfile.equals("prod")) {
            try {
                s3.deleteObject(BUCKET_NAME, fileName);
            }catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }else {
            File file = new File(fileName);
            file.delete();
        }
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
