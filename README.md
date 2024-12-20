# Demo Shop Service

A Spring Boot application that provides REST APIs for managing products and their discount policies.

## Features

- Product Management:
  - CRUD operations for products
  - Pagination support for product listing
  - Product price management

- Discount Policy Management:
  - Support for multiple discount types (AMOUNT, PERCENTAGE)
  - Threshold-based discount application
  - Multiple discount policies per product

- Discount Processing:
  - Calculate final price with applicable discounts
  - Support for stacking different types of discounts
  - Quantity-based discount calculation

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- H2 Database (in-memory)
- Flyway for database migrations
- OpenAPI/Swagger for API documentation
- Docker for containerization

## Running with Docker

### Prerequisites

- Docker
- Docker Compose

### Steps to Run

1. Clone the repository:
```bash
git clone https://github.com/VGolomoz/Demo-shop.git
cd demo-shop
```

2. Build and start the application using Docker Compose:
```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`

### Available Endpoints

- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:shopdb`
  - Username: `sa`
  - Password: `password`

## API Overview

### Products API

- `POST /v1/products` - Create a new product
- `GET /v1/products` - Get all products (paginated)
- `GET /v1/products/{id}` - Get product by ID
- `PUT /v1/products/{id}` - Update product
- `DELETE /v1/products/{id}` - Delete product
- `POST /v1/products/{id}/discount-policies/{policyId}` - Add discount policy to product
- `DELETE /v1/products/{id}/discount-policies/{policyId}` - Remove discount policy from product

### Discount Policies API

- `POST /v1/discount-policies` - Create a new discount policy
- `GET /v1/discount-policies` - Get all discount policies (paginated)
- `GET /v1/discount-policies/{id}` - Get discount policy by ID
- `PUT /v1/discount-policies/{id}` - Update discount policy
- `DELETE /v1/discount-policies/{id}` - Delete discount policy

### Discount Processing API

- `POST /v1/discount-processing/calculate` - Calculate final price with applicable discounts

## Development

The project uses an H2 in-memory database, which means data will be reset when the application restarts. This is suitable for development and testing purposes.

### Building without Docker

If you prefer to run the application without Docker, you'll need:
- JDK 17
- Gradle

Then you can run:
```bash
./gradlew bootRun
```

## API Documentation

Detailed API documentation is available through Swagger UI when the application is running. You can explore all endpoints, see request/response models, and test the APIs directly from the browser.
