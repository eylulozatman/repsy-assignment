package com.eylul.repsy.repsy_assignment.service;

import com.eylul.repsy.repsy_assignment.exception.PackageException;
import com.eylul.repsy.repsy_assignment.model.PackageEntity;
import com.eylul.repsy.repsy_assignment.repository.PackageRepository;
import com.eylul.repsy.repsy_assignment.service.storage.StorageStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final StorageStrategy storageStrategy;
    private final ObjectMapper objectMapper;

    public PackageService(PackageRepository packageRepository, 
                        StorageStrategy storageStrategy,
                        ObjectMapper objectMapper) {
        this.packageRepository = packageRepository;
        this.storageStrategy = storageStrategy;
        this.objectMapper = objectMapper;
    }

    public void deployPackage(String packageName, String version, 
                            MultipartFile packageFile, MultipartFile metaFile) throws Exception {
        
        validateFiles(packageFile, metaFile);
        
        // Parse and validate meta.json
        Map<String, Object> metaData = objectMapper.readValue(metaFile.getBytes(), Map.class);
        validateMetaData(metaData, packageName, version);

        // Store files
        storageStrategy.storeFile(packageFile, packageName, version);
        storageStrategy.storeFile(metaFile, packageName, version);

        // Save package metadata to database
        PackageEntity packageEntity = new PackageEntity(
                packageName,
                version,
                (String) metaData.get("author"),
                metaData
        );
        packageRepository.save(packageEntity);
    }

    public byte[] downloadPackage(String packageName, String version, String fileName) throws Exception {
        return storageStrategy.retrieveFile(packageName, version, fileName);
    }

    private void validateFiles(MultipartFile packageFile, MultipartFile metaFile) {
        if (packageFile.isEmpty() || metaFile.isEmpty()) {
            throw new PackageException("Both package.rep and meta.json files must be provided");
        }

        if (!packageFile.getOriginalFilename().endsWith(".rep")) {
            throw new PackageException("Package file must have .rep extension");
        }

        if (!metaFile.getOriginalFilename().endsWith(".json")) {
            throw new PackageException("Meta file must have .json extension");
        }
    }

    private void validateMetaData(Map<String, Object> metaData, String packageName, String version) {
        if (!packageName.equals(metaData.get("name"))) {
            throw new PackageException("Package name in URL doesn't match meta.json");
        }

        if (!version.equals(metaData.get("version"))) {
            throw new PackageException("Package version in URL doesn't match meta.json");
        }

        if (!metaData.containsKey("dependencies")) {
            throw new PackageException("Meta.json must contain dependencies array");
        }
    }
}