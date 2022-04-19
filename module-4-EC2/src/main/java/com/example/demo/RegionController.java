package com.example.demo;

import com.amazonaws.AmazonClientException;
import com.amazonaws.util.EC2MetadataUtils;
import com.example.demo.bean.DescribeBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionController {

    @RequestMapping("/info")
    DescribeBean hello() {
        try {
            return new DescribeBean(EC2MetadataUtils.getEC2InstanceRegion(),EC2MetadataUtils.getAvailabilityZone());
        } catch (AmazonClientException sce) {
            System.err.println("Ignoring failure to retrieve the region: " + sce.getMessage());
            return null;
        }
    }

}