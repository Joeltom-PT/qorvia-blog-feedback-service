package com.qorvia.blogfeedbackservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BlogFeedbackServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogFeedbackServiceApplication.class, args);
	}

}
