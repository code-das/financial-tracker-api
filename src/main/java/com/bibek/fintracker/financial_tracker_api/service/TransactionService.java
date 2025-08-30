package com.bibek.fintracker.financial_tracker_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job csvImportJob;

    public void processAndSaveTransactions(MultipartFile file, String username) {
        try {
            // 1. Save the uploaded file to a temporary location
            Path tempDir = Files.createTempDirectory("fintracker-");
            File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Saved temporary file to: {}", tempFile.getAbsolutePath());

            // 2. Create job parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", tempFile.getAbsolutePath())
                    .addString("username", username)
                    .addLong("timestamp", System.currentTimeMillis()) // Ensures the job is unique
                    .toJobParameters();

            // 3. Launch the batch job
            jobLauncher.run(csvImportJob, jobParameters);
            logger.info("Batch job started for user '{}' and file '{}'", username, file.getOriginalFilename());

        } catch (Exception e) {
            logger.error("Failed to process file and launch batch job", e);
            // In a real app, you'd throw a custom exception here
            throw new RuntimeException("Failed to process file", e);
        }
    }
}