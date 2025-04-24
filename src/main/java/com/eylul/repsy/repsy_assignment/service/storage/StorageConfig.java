package com.eylul.repsy.repsy_assignment.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class StorageConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(StorageConfig.class);
    
    @Value("${storage.strategy:file-system}")
    private String storageStrategy;
    
    @Value("${minio.endpoint:http://localhost:9000}")
    private String minioEndpoint;
    
    @Value("${minio.access-key:minioadmin}")
    private String minioAccessKey;
    
    @Value("${minio.secret-key:minioadmin}")
    private String minioSecretKey;
    
    @Value("${minio.bucket-name:repsy-packages}")
    private String minioBucketName;

    public StorageConfig() {
        logger.info("StorageConfig bean oluşturuluyor...");
    }

    @Bean
    public StorageStrategy storageStrategy() {
        logger.info("Storage strategy seçiliyor...");
        logger.debug("Konfigürasyon değerleri:");
        logger.debug("storage.strategy: {}", storageStrategy);
        logger.debug("minio.endpoint: {}", minioEndpoint);
        logger.debug("minio.access-key: {}", minioAccessKey != null ? "***MASKED***" : "NULL");
        logger.debug("minio.secret-key: {}", minioSecretKey != null ? "***MASKED***" : "NULL");
        logger.debug("minio.bucket-name: {}", minioBucketName);

        if ("file-system".equalsIgnoreCase(storageStrategy)) {
            logger.info("FileSystem storage strategy seçildi");
            return new FileStorageStrategy();
        } else if ("object-storage".equalsIgnoreCase(storageStrategy)) {
            logger.info("ObjectStorage strategy seçildi");
            return new ObjectStorageStrategy(
                    minioEndpoint,
                    minioAccessKey,
                    minioSecretKey,
                    minioBucketName
            );
        }
        
        throw new IllegalArgumentException("Geçersiz storage stratejisi: " + storageStrategy);
    }
}