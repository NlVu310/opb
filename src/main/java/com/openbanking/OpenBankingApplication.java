package com.openbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.openbanking.entity")
public class OpenBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenBankingApplication.class, args);
	}

}
