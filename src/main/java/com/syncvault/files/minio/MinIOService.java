package com.syncvault.files.minio;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinIOService {

    @Autowired
    private MinioClient minioClient;

    public void saveFile() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.makeBucket(
                MakeBucketArgs
                        .builder()
                        .bucket("user1")
                        .build());


        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket("user1")
                .object("Resume.pdf")
                .stream(new FileInputStream("/tmp/Resume.pdf"),-1,-1)
                .build());

    }
}