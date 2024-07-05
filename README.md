
## StayEase : Room Booking Application

### Overview

StayEase is a RESTful API service designed to streamline the room booking process for a hotel management aggregator application. This application is built using Spring Boot and MySQL to manage and persist data. The service includes authentication and authorization using JWT tokens and supports three roles: CUSTOMER, HOTEL_MANAGER, and ADMIN.


 
## Key Features :

- Simplified room booking system.
-   Assumptions:
    - Single type of room, bookings for two guests.
    - Any hotel manager can update any hotel details.
    - Check-in and check-out are handled by another service.
- JWT tokens for session management.
- Public and private API endpoints.
- User roles: CUSTOMER, HOTEL MANAGER, ADMIN.

## API Features :

### 1. User Registration and Login

- Users can register with an email address and password.
- Passwords are encrypted using BCrypt.
- Fields: Email, Password, First Name, Last Name, Role.
- Default role is "Customer" if not specified.
- JWT token generation upon successful login.

### 2. Hotel Management

- Store and manage hotel details.
- Fields: Hotel Name, Location, Description, Number of Available Rooms.
- Anyone can browse available hotels (public endpoint).
- Only the admin can create and delete hotels.
- Hotel managers can update hotel details.

### 3. Booking Management

- Customers can book rooms.
- Single room booking per request.
- Only hotel managers can cancel bookings.

## Requirements :

### 1. Logging
- Log information and errors using SLF4J.

### 2. Error Handling
- Graceful handling of common errors with appropriate HTTP codes.

### 3. Testing
- Basic unit tests with MockMvc and Mockito 

### 4. Deployment
- Generate a JAR file for the application.
- Instructions on how to run the application.

### 5. Documentation
- Descriptive README.md.
- Public Postman Collection.

## Project Structure

The project follows a layered architecture approach:

- `entities` : Contains the entity classes.
- `controllers`: Handles incoming HTTP requests and responses.
- `services`: Contains the logic for managing users books and rentals.
- `repositories`: Interacts with the MySQL Database.
- `exceptions` : Contains the custom exceptions and exception handler classes. 



## Setup Instructions :

### Prerequisites

- Java 11 or higher
- Gradle 6.0.8 or higher
- MySQL 8.0 or higher
- Git
- Postman


To run the application follow the following steps :

### Local Environment Setup :
#### 1. Clone the repository :

    git clone https://github.com/Omkar2363/StayEase.git


### 2. Set up the MySQL database :

- Update the application.properties file with your MySQL database details:

        spring.datasource.url=jdbc:mysql://localhost:3306/stayease?createDatabaseIfNotExist=true
        spring.datasource.username=<your-username>
        spring.datasource.password=<your-password>
        spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
        spring.jpa.hibernate.ddl-auto=update
        spring.jpa.show-sql=true

#### 2. Navigate to the project directory :

    cd StayEase

#### 3. Build the project :

    gradle build 

#### 4. Run the application :

To run the application use following command.

    java -jar build/libs/StayEase-0.0.1-SNAPSHOT.jar


#### 5. Access the APIs:

##### a. Local Server : 
Once the application is running locally, you can access the API at 

    http://localhost:8080

##### b. Deployed Server : 
As the application is deployed successfully, you can access the API as mentioned
    
    https://stayeasedeploy.onrender.com/swagger-ui/index.html



## Running Tests :

Use the following command to run the unit tests :

    gradle test
## Endpoints :

### 1. Public Endpoints 
#### i. Accessible to everyone 
- User Registration : POST /register
- User Login  : POST /login
- Browse Hotel : GET /hotels
- Browse Hotel : GET /hotels/{hotelId}


### 2. Private Endpoints

#### i. Accessible to Authenticated USERS
- User Update : PUT /users/{userId}
- User Book Room : POST /hotels/{hotelId}/book

#### ii. ADMIN only 
- Create Hotel : POST /hotels
- Delete Hotel : Delete /hotels/{hotelId}

#### iii. HOTEL_MANAGER only
- Update Hotel : PUT /hotels/{hotelId}
- Cancel Booking : DELETE /bookings/{bookingId}

#### iv. Both ADMIN and HOTEL_MANAGER
- Read USER : GET /users
- Read USER : GET /users/{userId}
- Read USER : GET /users/emailId
- Delete USER : DELETE /users/{userId}
- Update Hotel : PUT /hotels/{hotelId}
- Read Booking  : GET /bookings/{bookingId}
- Read Bookings : GET /bookings 

#### NOTE : Use the following link to read all the API Documentation.

[API Documentation using OpenAPI](https://stayeasedeploy.onrender.com/swagger-ui/index.html)
## Postman Collection

A Postman collection has been included to test the API endpoints. Import the collection into Postman and start testing the API.

[StayEase Postman Collection Link](https://www.postman.com/omkar2363/workspace/stayease/collection/28208818-8edb6a1f-eaed-4095-8966-0dac8cc77734?action=share&creator=28208818)
## Validation and Error Handing

- Basic validation is implemented to check student is registered for subject or not before registering for the exam of the subject.
- Common errors are handled, and appropriate HTTP status codes are returned (e.g., 404 for User not found).

## Licence

All the copy rights belongs to Omkar Yadav.
