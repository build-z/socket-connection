package com.xiaolvche.cloudconnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
public class CloudConnectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudConnectionApplication.class, args);
	}

}
