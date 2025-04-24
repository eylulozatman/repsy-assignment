package com.eylul.repsy.repsy_assignment.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageStrategy {
    void storeFile(MultipartFile file, String packageName, String version) throws Exception;
    byte[] retrieveFile(String packageName, String version, String fileName) throws Exception;
}