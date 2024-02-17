package com.blumeglobal.springor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
//@EnableSwagger2
public class SpringOrApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringOrApplication.class, args);
	}
}
