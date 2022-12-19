package com.smilegate.authcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AuthcloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthcloudApplication.class, args);
	}
}