package com.openbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class OpenBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenBankingApplication.class, args);
	}

}
