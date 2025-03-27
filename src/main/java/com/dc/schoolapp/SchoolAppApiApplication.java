package com.dc.schoolapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
        exclude = HibernateJpaAutoConfiguration.class,
        scanBasePackages = {
                "com.dc.schoolapp.config",
                "com.dc.schoolapp.filter",
                "com.dc.schoolapp.utils"
        })
public class SchoolAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolAppApiApplication.class, args);
    }

}
