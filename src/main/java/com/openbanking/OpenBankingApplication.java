package com.openbanking;

import com.openbanking.utils.SSLUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpenBankingApplication {

	public static void main(String[] args) {
		try {
			SSLUtils.setupCustomHostnameVerifier();
		} catch (Exception e) {
		}
		SpringApplication.run(OpenBankingApplication.class, args);
	}

}
