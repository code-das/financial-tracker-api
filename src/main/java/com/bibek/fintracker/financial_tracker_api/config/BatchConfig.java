package com.bibek.fintracker.financial_tracker_api.config;

import com.bibek.fintracker.financial_tracker_api.batch.TransactionItemProcessor;
import com.bibek.fintracker.financial_tracker_api.model.Transaction;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing // 1. Added for clarity and best practice
public class BatchConfig {

    // 2. All dependencies are now class fields
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    // 2. All dependencies are injected via the constructor
    public BatchConfig(UserRepository userRepository, JobRepository jobRepository, PlatformTransactionManager transactionManager, EntityManagerFactory entityManagerFactory) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> csvReader(@Value("#{jobParameters['filePath']}") String filePath) {
        // 1. Create a custom conversion service
        DefaultConversionService conversionService = new DefaultConversionService();
        // 2. Add a converter from String to LocalDate
        conversionService.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
            }
        });

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("date", "description", "amount")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Transaction.class);
                    // 3. Set the custom conversion service
                    setConversionService(conversionService);
                }})
                .linesToSkip(1)
                .build();
    }

    @Bean
    @StepScope
    public TransactionItemProcessor transactionItemProcessor(@Value("#{jobParameters['username']}") String username) {
        return new TransactionItemProcessor(userRepository, username);
    }

    @Bean
    public JpaItemWriter<Transaction> transactionItemWriter() {
        return new JpaItemWriterBuilder<Transaction>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Step csvImportStep(ItemReader<Transaction> reader,
                              ItemProcessor<Transaction, Transaction> processor,
                              ItemWriter<Transaction> writer) {
        return new StepBuilder("csvImportStep", jobRepository) // 2. Now uses the class field
                .<Transaction, Transaction>chunk(10, transactionManager) // 2. Now uses the class field
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job csvImportJob(Step csvImportStep) {
        return new JobBuilder("csvImportJob", jobRepository) // 2. Now uses the class field
                .start(csvImportStep)
                .build();
    }
}