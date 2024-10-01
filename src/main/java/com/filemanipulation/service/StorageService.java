package com.filemanipulation.service;

import com.filemanipulation.entity.FileData;
import com.filemanipulation.repository.StorageRepository;
import com.filemanipulation.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    StorageRepository storageRepository;

    public String uploadFile(MultipartFile file) {
        // Get file name, content type and bytes
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        byte[] fileBytes;

        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }

        // Build FileData object using builder
        FileData fileData = FileData.builder()
                .name(fileName)
                .type(fileType)
                .filePath(fileBytes)
                .build();

        storageRepository.save(fileData);

        return "File uploaded successfully: " + fileName;
    }
    public byte[] downloadImage(String fileName){
        Optional<FileData> dbImageData = storageRepository.findByName(fileName);
        byte[] images= FileUtils.decompressImage(dbImageData.get().getFilePath());
        return images;
    }
    public byte[] downloadPdf(String fileName) {
        Optional<FileData> dbPdfData = storageRepository.findByName(fileName);

        if (dbPdfData.isPresent()) {
            // Assuming the PDF is stored as a byte array
            return dbPdfData.get().getFilePath(); // Ensure that this is correct data (byte[])
        }

        // Return null or throw an exception if the file is not found
        return null;
    }

}
