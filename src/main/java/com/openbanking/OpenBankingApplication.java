package com.openbanking;

import com.openbanking.utils.SSLUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class OpenBankingApplication {

	public static void main(String[] args) {
		SSLUtil.disableCertificateChecking();
		SpringApplication.run(OpenBankingApplication.class, args);
	}

}
