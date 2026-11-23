package com.syncvault.files.minio;

import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Slf4j
@Service
public class MinIOService {

    @Autowired
    private MinioClient minioClient;

    private final String BUCKET_1 = "filebucket1";

    public InputStream getFile(String objectKey) throws Exception {
        log.info("Getting object key {}", objectKey);
        return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_1)
                            .object(objectKey)
                            .build());
    }

    public ObjectWriteResponse saveFile(MultipartFile file){
        checkBucket();
        ObjectWriteResponse objectWriteResponse = null;
        try {
            objectWriteResponse = minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(BUCKET_1)
                    .object("file")
                    .stream(file.getInputStream(), file.getSize(), file.getSize())
                    .build());
        } catch (MinioException | GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        return objectWriteResponse;
    }

    private void checkBucket() {
        boolean bucketExists = false;
        try {
            bucketExists = minioClient
                    .bucketExists(BucketExistsArgs.builder()
                            .bucket(BUCKET_1)
                            .build());
        } catch(MinioException | GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        if(!bucketExists) createBucket();
    }

    private void createBucket() {
        try {
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(BUCKET_1)
                            .build());
        } catch (MinioException | GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}