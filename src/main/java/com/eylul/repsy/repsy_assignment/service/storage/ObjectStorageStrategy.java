package com.eylul.repsy.repsy_assignment.service.storage;

import io.minio.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ObjectStorageStrategy implements StorageStrategy {
    private final MinioClient minioClient;
    private final String bucketName;

    public ObjectStorageStrategy(String endpoint, String accessKey, String secretKey, String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    @Override
    public void storeFile(MultipartFile file, String packageName, String version) throws Exception {
        String objectName = packageName + "/" + version + "/" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .build());
    }

    @Override
    public byte[] retrieveFile(String packageName, String version, String fileName) throws Exception {
        String objectName = packageName + "/" + version + "/" + fileName;
        try (GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())) {
            return response.readAllBytes();
        }
    }
}