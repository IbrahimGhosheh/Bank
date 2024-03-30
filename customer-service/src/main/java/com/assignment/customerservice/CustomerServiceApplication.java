package com.assignment.customerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Customer Service API",
                version = "1.0",
                description = "Documentation Customer Service API v1.0"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8888/customer-service",
                        description = "api gateway server"
                ),
                @Server(
                        url = "http://localhost:8081",
                        description = "local server"
                )
        }
)
@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
