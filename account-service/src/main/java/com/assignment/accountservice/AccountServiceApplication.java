package com.assignment.accountservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Account Service API",
				version = "1.0",
				description = "Documentation Account Service API v1.0"
		),
		servers = {
				@Server(
						url = "http://localhost:8888/account-service",
						description = "api gateway server"
				),
				@Server(
						url = "http://localhost:8080",
						description = "local server"
				)
		}
)
@SpringBootApplication
@EnableFeignClients
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
