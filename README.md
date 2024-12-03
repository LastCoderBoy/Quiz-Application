****Quiz Application****

**Overview**
This is a backend-focused Quiz Application built using Java Spring Boot. It utilizes a microservices architecture to handle quiz creation, question management, and answer evaluation. The project demonstrates best practices in software design, using components like a service registry, API gateway, and separate databases for each microservice.

**Features**
  - Create quizzes with multiple-choice questions.
  - Manage questions and answers efficiently.
  - Evaluate answers and calculate scores.
  - Microservices architecture with modular components.
  - Service registry for microservice discovery and communication.
  - API Gateway for request routing.
  - Separate MySQL databases for quiz and question services.

**Architecture**
The application follows a Microservices Architecture with the following components:

Quiz Microservice: 
    * Handles quiz creation and management.
Question Microservice: 
    * Manages the pool of questions and their details.
Service Registry: 
    * Facilitates service discovery between microservices.
API Gateway:
    * Routes requests to the appropriate microservice.

**Database Structure**
Each microservice uses its own MySQL database:

Quiz Database: 
    - Stores quiz metadata and relationships.
Question Database: 
    - Stores question details and answers.

**Technologies Used**
Java Spring Boot: Backend framework.
MySQL: Database management.
Eureka: Service registry for microservice discovery.
Spring Cloud Gateway: API Gateway.
Postman: API testing.
Maven: Build and dependency management.
Getting Started
Prerequisites
Java 17 or higher.
MySQL Server installed and running.
Maven installed.
Postman (optional, for testing).

**Installation**
Clone the repository:

git clone https://github.com/LastCoderBoy/quiz-application.git

Navigate to the project directory:

cd quiz-application

**Build the project:**

mvn clean install

**Run the microservices:**

Start the Service Registry first.
Start the API Gateway.
Run the Quiz Microservice and Question Microservice.
Configure MySQL databases for the services and update the application.properties files accordingly.
