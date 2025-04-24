package com.eylul.repsy.repsy_assignment.controller;


import com.eylul.repsy.repsy_assignment.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> deployPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile) {
        
        try {
            packageService.deployPackage(packageName, version, packageFile, metaFile);
            return ResponseEntity.ok("Package deployed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error during package deployment: " + e.getMessage());
        }
    }

    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<byte[]> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) {
        
        if (!fileName.equals("package.rep") && !fileName.equals("meta.json")) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            byte[] fileContent = packageService.downloadPackage(packageName, version, fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}