package com.stride.stride_common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Main Spring Boot application for the Stride Common Library.
 * 
 * This is a utility library that provides shared components for Stride microservices.
 * Database auto-configuration is excluded since this library doesn't require its own database -
 * consuming services will provide their own data configuration.
 */
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class StrideCommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrideCommonApplication.class, args);
	}

}
