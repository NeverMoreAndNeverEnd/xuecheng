package com.xuecheng.manage.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.xuecheng.api")
@ComponentScan(basePackages = "com.xuecheng.manage.cms")
@ComponentScan(basePackages = "com.xuecheng.framework")
@EntityScan("com.xuecheng.framework.domain")
public class XcServiceManageCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcServiceManageCmsApplication.class, args);
    }

}
