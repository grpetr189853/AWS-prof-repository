package com.epam.aws.training.config;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.epam.aws.training.config.properties.ImageBucketProperties;
import com.epam.aws.training.config.properties.LambdaClientProperties;
import com.epam.aws.training.config.properties.SNSClientProperties;
import com.epam.aws.training.config.properties.SQSClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationConfig {

  private final ImageBucketProperties imageBucketProperties;
  private final SNSClientProperties snsClientProperties;
  private final SQSClientProperties sqsClientProperties;
  private final LambdaClientProperties lambdaClientProperties;

  @Bean
  public AmazonS3 s3Client() {
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(imageBucketProperties.getRegion())
        .build();
  }

  @Bean
  public TransferManager transferManager(AmazonS3 s3Client) {
    return TransferManagerBuilder.standard()
        .withS3Client(s3Client)
        .build();
  }

  @Bean
  public AmazonSNS snsClient(){
    return AmazonSNSClient
        .builder()
        .withRegion(snsClientProperties.getRegion())
        .build();
  }

  @Bean
  public AmazonSQS sqsClient(){
    return AmazonSQSClient
        .builder()
        .withRegion(sqsClientProperties.getRegion())
        .build();
  }
  
  @Bean
  public AWSLambda awsLambda(){
    return AWSLambdaClient
            .builder()
            .withRegion(lambdaClientProperties.getRegion())
            .build();
  }
}
