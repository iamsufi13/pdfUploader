package com.filemanipulation.controller;

import com.filemanipulation.entity.FileData;
import com.filemanipulation.repository.StorageRepository;
import com.filemanipulation.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    private StorageService service;

    @Autowired
    private StorageRepository storageRepository;
    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        byte[] pdfData = file.getBytes();

        FileData fileData = new FileData();
        fileData.setName(file.getOriginalFilename());
        fileData.setFilePath(pdfData);
        storageRepository.save(fileData);

        return "File uploaded successfully: " + file.getOriginalFilename();
    }


    @GetMapping("/download-pdf/{fileName}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String fileName) {
        byte[] pdfData = service.downloadPdf(fileName);

        if (pdfData == null || pdfData.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

}
