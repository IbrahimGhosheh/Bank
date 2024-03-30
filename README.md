
# Bank Management System

This project is a microservices-based application designed to manage bank customers and their associated accounts.

## Prerequisites

* Java:  JDK 17 or higher
* Maven: used for building the project
* Docker: used for running the PostgreSQL and Kafka services.
## Usage
1- Clone the repository
``` 
git clone https://github.com/IbrahimGhosheh/Bank.git
```

2- Navigate to the project directory
``` 
cd <project-directory>
```
3- Start PostgreSQL and Kafka using Docker Compose:

```
docker-compose up -d
```

4- Start each service:

```
# For Discovery Service
cd discovey-service
mvn spring-boot:run

# For Customer Service
cd customer-service
mvn spring-boot:run

# For Account Service
cd account-service
mvn spring-boot:run

# For Gateway Service
cd gateway-service
mvn spring-boot:run
```

5- After starting the services, you can access the Swagger UI documentation:

* For Customer Service: http://localhost:8081/swagger-ui.html
* For Account Service: http://localhost:8080/swagger-ui.html
* Through the API Gateway for both services: http://localhost:8888/swagger-ui.html
## Architecture

Our project is a microservices-based system consisting of the following modules:

1- **API Gateway Service**: This module uses Spring Cloud Gateway as a proxy/gateway for routing requests to the appropriate microservices.

2- **Discovery Service**: This module serves as the service registry and uses either Spring Cloud Netflix Eureka as the embedded discovery server.

3- **Customer Service**: This microservice allows for the management of customer information and includes CRUD operations for a customer entity. It communicates with the account service .

4- **Account Service**: This microservice manages account-related operations, including CRUD operations for account entities. It communicates with the customer service.

### Database
We use PostgreSQL for storing customer and account information.

### Service Communication
* Account Service to Customer Service: The account service retrieves customer information through REST API calls to the customer service.

* Customer Service to Account Service (Event-driven): The customer service sends customer delete events through Kafka to the account service, triggering the deletion of associated accounts.
## Shortcomings

**Security**: The project lack comprehensive security measures. Enhancements can be made to include features like authentication, authorization, and data encryption to ensure secure communication and data storage.