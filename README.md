
# Car Management API

This is a Spring Boot application that provides an API to manage car operations such as creating cars, starting/stopping the engine, refueling, and checking engine and fuel status. The application uses PostgreSQL as its database, Liquibase for schema versioning, and JPA for repository management.

---

## Table of Contents
- [Setup Instructions](#setup-instructions)
- [Database Configuration](#database-configuration)
- [Liquibase Setup](#liquibase-setup)
- [Postman Setup](#postman-setup)
- [API Endpoints](#api-endpoints)
- [Exception Handling](#exception-handling)
- [Testing](#testing)

---

## Setup Instructions

1. **Clone the repository**
   ```bash
   git  https://github.com/IstvanOzsvath225/sb_car_api_accenture.git
   ```

2. **Database Configuration**
   
   The application uses PostgreSQL as the database. Make sure you have PostgreSQL installed and running locally or remotely.

3. **Configure Application Properties**

   Modify the `application.properties` file to point to your PostgreSQL instance. Below is an example configuration for `application.properties` for database connection:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/car_db
   spring.datasource.username=username
   spring.datasource.password=password
   ```

4. **Install Dependencies**

   Ensure you have Maven installed. Install dependencies by running:
   ```bash
   mvn clean install
   ```

5. **Run the Application**

   To run the application:
   ```bash
   mvn spring-boot:run
   ```

   The server should be running at: `http://localhost:8080`.

---

## Database Configuration

- **PostgreSQL**: The application uses PostgreSQL as the primary database. Ensure you have the correct credentials in `application.properties` as per the instructions in the setup section.
- **JPA (Java Persistence API)**: JPA is used for ORM (Object Relational Mapping) to interact with the PostgreSQL database.
- **Liquibase**: Liquibase is used for database schema management and versioning.

---

## Liquibase Setup

Liquibase is used to handle database migrations and schema changes.

1. **Define ChangeLogs**: 
   ChangeLogs are defined in `db/changelog/db.changelog-master.xml`. You can add new changesets here for versioning.
   
2. **Run Migrations**: 
   When you run the application, Liquibase automatically applies any pending migrations to your database. No manual setup is required.
3. **Build Database**:
   When you run the application, database table will be created by liquibase from `001-create-base-tables.xml`'
---
## Postman setup
`resources/postmanSet/Cars.postman_collection.json` contains available requests for Postman. It can be imported to Postman.

---
## API Endpoints
Here are the available API endpoints for managing cars:

### `POST /api/cars/createCar`
Creates a new car with an engine, fuel tank, and dashboard.

- **Response**: Returns the created `Car` object.
  
### `POST /api/cars/{carId}/start`
Starts the car if conditions are met (sufficient fuel and functional engine).

- **Path Parameter**: `carId` (Long) - The ID of the car.
- **Response**: Returns the updated `Car` object.
- **Exceptions**:
  - `EngineFailureException`: Thrown if the engine fails to start.
  - `FuelEmptyException`: Thrown if there is insufficient fuel to start.

### `POST /api/cars/{carId}/stop`
Stops the car by turning off the engine.

- **Path Parameter**: `carId` (Long) - The ID of the car.
- **Response**: Returns the updated `Car` object.
  
### `POST /api/cars/{carId}/refuel`
Refuels the car with the specified amount of fuel.

- **Path Parameter**: `carId` (Long) - The ID of the car.
- **Request Parameter**: `amount` (int) - The amount of fuel to add.
- **Response**: Returns the updated `Car` object with the new fuel level.

### `GET /api/cars/{carId}/checkFuelLevel`
Checks the current fuel level of the car.

- **Path Parameter**: `carId` (Long) - The ID of the car.
- **Response**: Returns the fuel level (int).

### `GET /api/cars/{carId}/isEngineRunning`
Checks if the car's engine is currently running.

- **Path Parameter**: `carId` (Long) - The ID of the car.
- **Response**: Returns a boolean indicating whether the engine is running.

---

## Exception Handling

The application has custom exceptions to handle specific business logic:

1. **CarNotFoundException**: Thrown when a car with the specified ID does not exist.
2. **EngineFailureException**: Thrown when the engine fails to start.
3. **FuelEmptyException**: Thrown when there is not enough fuel to start the car.

These exceptions are logged using SLF4J and the logs can be viewed in the application logs.

---

## Testing

The application includes unit tests for the `CarService` class to ensure that business logic is working as expected.

### Test Frameworks Used:
- **JUnit 5**: For unit testing.
- **Mockito**: For mocking dependencies like `CarRepository`.
---

## Conclusion

This application provides a simple way to manage cars, handle engine and fuel status, and logs detailed messages for actions performed on the cars. The setup is easy with PostgreSQL, Liquibase for schema management, and JPA for database interactions. 
