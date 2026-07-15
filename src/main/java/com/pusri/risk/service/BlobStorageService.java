package com.pusri.risk.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class BlobStorageService {

    @Value("${azure.storage.connection-string:}")
    private String connectionString;

    @Value("${azure.storage.container-name:risk-documents}")
    private String containerName;

    private BlobContainerClient containerClient;

    @PostConstruct
    public void init() {
        if (connectionString != null && !connectionString.isEmpty()) {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
            if (!this.containerClient.exists()) {
                this.containerClient.create();
            }
        }
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty() || containerClient == null) {
            return null;
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String blobPath = folder + "/" + filename;
        
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        
        return "uploads/" + blobPath;
    }

    public InputStream downloadFile(String blobPath) {
        if (containerClient == null) return null;
        // blobPath comes in as e.g., sasaran/uuid_name.pdf.
        // Or if it comes in as uploads/sasaran/uuid_name.pdf, we strip 'uploads/'
        if (blobPath.startsWith("/uploads/")) {
            blobPath = blobPath.substring(9);
        } else if (blobPath.startsWith("uploads/")) {
            blobPath = blobPath.substring(8);
        }
        
        BlobClient blobClient = containerClient.getBlobClient(blobPath);
        if (blobClient.exists()) {
            return blobClient.openInputStream();
        }
        return null;
    }
}
