package com.xuecheng.manage.cms.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain")
@ComponentScan(basePackages = "com.xuecheng.framework")
@ComponentScan(basePackages = "com.xuecheng.manage.cms.client")
public class XcServiceManageCmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcServiceManageCmsClientApplication.class, args);
    }

}