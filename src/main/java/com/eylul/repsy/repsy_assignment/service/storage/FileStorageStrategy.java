package com.eylul.repsy.repsy_assignment.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageStrategy implements StorageStrategy {
    private static final String BASE_PATH = "storage/files";

    @Override
    public void storeFile(MultipartFile file, String packageName, String version) throws IOException {
        Path packageDir = Paths.get(BASE_PATH, packageName, version);
        Files.createDirectories(packageDir);
        Path filePath = packageDir.resolve(file.getOriginalFilename());
        file.transferTo(filePath);
    }

    @Override
    public byte[] retrieveFile(String packageName, String version, String fileName) throws IOException {
        Path filePath = Paths.get(BASE_PATH, packageName, version, fileName);
        return Files.readAllBytes(filePath);
    }
}