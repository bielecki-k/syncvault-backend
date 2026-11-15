package com.syncvault.files.minio;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MinIOConfiguration {

    @Bean
    public MinioClient getMinIOClient(){
                return MinioClient.builder()
                        .endpoint("http://127.0.0.1:9000")
                        .credentials("minioadmin", "minioadmin123")
                        .build();
    }

}
