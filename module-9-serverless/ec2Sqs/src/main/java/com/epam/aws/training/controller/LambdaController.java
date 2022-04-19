package com.epam.aws.training.controller;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lambda")
@RequiredArgsConstructor
public class LambdaController {

    @Value("${lambda.functionARN}")
    private String function;
    @Value("${lambda.region}")
    private String region;


    @PutMapping("/action")
    public ResponseEntity<Object> lambdaAction() {
        try {
            AWSLambda awsLambda = AWSLambdaClient
                    .builder()
                    .withRegion(region)
                    .build();
            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(function)
                    .withPayload("{\"detail-type\": \"AWP application\"}");
            awsLambda.invoke(invokeRequest);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
