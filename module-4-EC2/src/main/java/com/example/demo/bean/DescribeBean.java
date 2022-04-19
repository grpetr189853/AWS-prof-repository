package com.example.demo.bean;

import java.util.Objects;

public class DescribeBean {
    
    private String region;
    private String az;

    public DescribeBean(String region, String az) {
        this.region = region;
        this.az = az;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAz() {
        return az;
    }

    public void setAz(String az) {
        this.az = az;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DescribeBean that = (DescribeBean) o;
        return Objects.equals(region, that.region) && Objects.equals(az, that.az);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, az);
    }

    @Override
    public String toString() {
        return "DescribeBean{" +
                "region='" + region + '\'' +
                ", az='" + az + '\'' +
                '}';
    }
}
