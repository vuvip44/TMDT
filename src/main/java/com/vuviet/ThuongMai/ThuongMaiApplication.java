package com.vuviet.ThuongMai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@SpringBootApplication
@EnableScheduling
@EnableWebSecurity
@EnableAsync
public class ThuongMaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThuongMaiApplication.class, args);
	}

}
