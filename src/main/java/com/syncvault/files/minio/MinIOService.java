package com.syncvault.files.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class MinIOService {

    @Autowired
    private MinioClient minioClient;

    private final String BUCKET_1 = "filebucket1";

    public void saveFile(MultipartFile file){
        checkBucket();

        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(BUCKET_1)
                    .object("file")
                    .stream(file.getInputStream(), file.getSize(), file.getSize())
                    .build());
        } catch (MinioException | GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
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