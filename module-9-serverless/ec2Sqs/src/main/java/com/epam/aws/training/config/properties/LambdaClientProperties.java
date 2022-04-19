package com.epam.aws.training.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "lambda")
@Component
public class LambdaClientProperties {

    private String functionARN;
    private String region;
}
