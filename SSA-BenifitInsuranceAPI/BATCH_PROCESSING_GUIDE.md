# Spring Batch Processing - Business Logic Guide

## Architecture Overview

```
Reader → Processor → Writer
  ↓         ↓          ↓
Read   Transform   Output
Data   & Filter    Data
```

---

## Step 1: ItemReader (Read Data)
**Purpose:** Fetch data from source (Database, File, API)

```java
@Bean
public RepositoryItemReader<ElgibilityDetailsEntity> reader() {
    Map<String, Sort.Direction> sorts = new HashMap<>();
    sorts.put("caseNo", Sort.Direction.ASC);
    
    return new RepositoryItemReaderBuilder<ElgibilityDetailsEntity>()
            .name("eligibility-reader")
            .repository(eligibilityRepo)
            .methodName("findAll")
            .pageSize(10)  // Read 10 records at a time
            .sorts(sorts)
            .build();
}
```

---

## Step 2: ItemProcessor (Business Logic)
**Purpose:** Transform and validate data

Example implementations:

### Option A: Simple Filtering
```java
@Component
public class EligibilityProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibilityDetails> {
    
    @Override
    public ElgibilityDetails process(ElgibilityDetailsEntity entity) throws Exception {
        // Skip records that are not approved
        if (!entity.getPlanStatus().equalsIgnoreCase("approved")) {
            return null;  // Null = skip this record
        }
        
        ElgibilityDetails details = new ElgibilityDetails();
        BeanUtils.copyProperties(entity, details);
        return details;
    }
}
```

### Option B: Complex Business Logic
```java
@Component
public class BenefitCalculationProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibilityDetails> {
    
    @Autowired
    private BenefitCalculationService benefitService;
    
    @Override
    public ElgibilityDetails process(ElgibilityDetailsEntity entity) throws Exception {
        // Validate data
        if (entity.getHolderSSN() == null || entity.getHolderSSN() <= 0) {
            throw new IllegalArgumentException("Invalid SSN for case: " + entity.getCaseNo());
        }
        
        ElgibilityDetails details = new ElgibilityDetails();
        BeanUtils.copyProperties(entity, details);
        
        // BUSINESS LOGIC: Calculate benefit amount
        double benefitAmount = benefitService.calculateBenefit(
            entity.getPlanName(),
            entity.getPlanEndDate(),
            entity.getHolderAge()
        );
        details.setBenifitAmt(benefitAmount);
        
        // BUSINESS LOGIC: Validate amount
        if (benefitAmount < 100) {
            return null;  // Skip low benefit amounts
        }
        
        // BUSINESS LOGIC: Apply adjustments
        details.setBenifitAmt(applyTaxAdjustments(benefitAmount));
        
        return details;
    }
    
    private double applyTaxAdjustments(double amount) {
        // Apply 10% tax deduction
        return amount * 0.90;
    }
}
```

### Option C: With Logging & Error Handling
```java
@Component
public class EnhancedEligibilityProcessor implements ItemProcessor<ElgibilityDetailsEntity, ElgibilityDetails> {
    
    private static final Logger logger = LoggerFactory.getLogger(EnhancedEligibilityProcessor.class);
    
    @Autowired
    private ValidationService validationService;
    
    @Override
    public ElgibilityDetails process(ElgibilityDetailsEntity entity) throws Exception {
        try {
            // Step 1: Validate
            if (!validationService.isValid(entity)) {
                logger.warn("Invalid record: {}", entity.getCaseNo());
                return null;
            }
            
            // Step 2: Transform
            ElgibilityDetails details = transformEntity(entity);
            
            // Step 3: Enrich with additional data
            enrichWithBankDetails(details);
            
            logger.info("Successfully processed case: {}", entity.getCaseNo());
            return details;
            
        } catch (Exception e) {
            logger.error("Error processing case {}: {}", entity.getCaseNo(), e.getMessage());
            throw e;
        }
    }
    
    private ElgibilityDetails transformEntity(ElgibilityDetailsEntity entity) {
        ElgibilityDetails details = new ElgibilityDetails();
        BeanUtils.copyProperties(entity, details);
        return details;
    }
    
    private void enrichWithBankDetails(ElgibilityDetails details) {
        // Fetch additional bank info if needed
        if (details.getBankName() != null) {
            // Add validation, formatting, etc.
        }
    }
}
```

---

## Step 3: ItemWriter (Output Data)
**Purpose:** Write processed data to destination

### Option A: CSV File
```java
@Bean
public FlatFileItemWriter<ElgibilityDetails> csvWriter() {
    return new FlatFileItemWriterBuilder<ElgibilityDetails>()
            .name("csv-writer")
            .resource(new FileSystemResource("beneficiaries.csv"))
            .delimited()
            .delimiter(",")
            .names("caseNo", "holderName", "holderSSN", "planName", "benifitAmt")
            .build();
}
```

### Option B: Database
```java
public class DatabaseItemWriter implements ItemWriter<ElgibilityDetails> {
    
    @Autowired
    private OutputRepository outputRepo;
    
    @Override
    public void write(Chunk<? extends ElgibilityDetails> items) throws Exception {
        List<OutputEntity> entities = items.getItems().stream()
            .map(this::convertToEntity)
            .collect(Collectors.toList());
        
        outputRepo.saveAll(entities);
    }
}
```

### Option C: API Call
```java
public class ApiItemWriter implements ItemWriter<ElgibilityDetails> {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public void write(Chunk<? extends ElgibilityDetails> items) throws Exception {
        for (ElgibilityDetails detail : items.getItems()) {
            restTemplate.postForObject(
                "http://payment-service/send-benefit",
                detail,
                Void.class
            );
        }
    }
}
```

---

## Step 4: Configuration
```java
@Configuration
public class BatchConfig {

    @Bean
    public Job benefitProcessingJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("benefitJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, 
                      RepositoryItemReader<ElgibilityDetailsEntity> reader,
                      BenefitCalculationProcessor processor,
                      FlatFileItemWriter<ElgibilityDetails> writer) {
        
        return new StepBuilder("step1", jobRepository)
                .<ElgibilityDetailsEntity, ElgibilityDetails>chunk(100)  // Process 100 rows at a time
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .retry(IOException.class)
                .retryLimit(3)
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .listener(stepExecutionListener())
                .build();
    }

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                System.out.println("Step started: " + stepExecution.getStepName());
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                System.out.println("Records processed: " + stepExecution.getWriteCount());
                System.out.println("Records skipped: " + stepExecution.getSkipCount());
                return stepExecution.getExitStatus();
            }
        };
    }
}
```

---

## Step 5: Service to Trigger Batch Job
```java
@Service
public class BatchJobService {
    
    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job benefitProcessingJob;
    
    public void startBenefitProcessing() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();
        
        JobExecution execution = jobLauncher.run(benefitProcessingJob, params);
        
        System.out.println("Job Status: " + execution.getStatus());
        System.out.println("Job ID: " + execution.getId());
    }
}
```

---

## Common Business Logic Patterns

### 1. Filtering
```java
if (condition) {
    return null;  // Skip this record
}
return processedData;
```

### 2. Enrichment
```java
details.setCalculatedField(calculateValue(entity));
details.setMappedField(mapExternalData(entity));
return details;
```

### 3. Validation
```java
if (!isValid(entity)) {
    throw new ValidationException("Invalid data");
}
return processedData;
```

### 4. Transformation
```java
ElgibilityDetails details = new ElgibilityDetails();
BeanUtils.copyProperties(entity, details);
// Modify specific fields
details.setFormattedDate(formatDate(entity.getPlanEndDate()));
return details;
```

---

## Execution Flow Summary

1. **Reader** reads 10 records from DB
2. **Processor** validates and transforms each record
3. **Writer** writes processed records to CSV or DB
4. **Repeat** until all data is processed
5. **Listener** logs statistics

---

## Key Points

✅ **Processor returns null** = Skip the record
✅ **Processor returns object** = Write it
✅ **Processor throws exception** = Fail (unless retry/skip configured)
✅ **Chunk size** = How many records to process before writing
✅ **Fault tolerance** = Handle errors gracefully

