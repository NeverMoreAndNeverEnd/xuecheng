package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class XcGovernCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcGovernCenterApplication.class, args);
    }

}
