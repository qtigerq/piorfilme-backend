# About

This project was developed as part of a backend technical challenge for Outsera.

---

# Autor

Tiago Bruno de Melo - Software Engineer

---

# Worst Movie from Golden Raspberry Awards

A RESTful API built as a technical test case using Java 21 and Spring Boot.  
It exposes endpoints to query statistics about the worst movies nominated for the Golden Raspberry Awards.

---

## Technologies Used

- Java 21
- Spring Boot 3.5.6
- Maven
- Spring Web
- Spring Data JPA
- H2 In-Memory Database
- Docker

---

## Requirements

Make sure the following tools are installed on your system:

- [Java 21 JDK](https://jdk.java.net/21/)
- [Maven](https://maven.apache.org/)

---

####### How to Run the Project (With or without Docker) ######
##############################################################

################ 1. Clone the Repository #####################

```bash
git clone https://github.com/qtigerq/piorfilme-backend.git
cd piorfilme-backend
##############################################################

### Without Docker
########### 2A. Install Dependencies and Build ################

- mvn clean install
##############################################################

################ 3A. Run the Application ######################

- mvn spring-boot:run

The application will start on: http://localhost:8080
##############################################################

### With docker
######### 2A. Run Application via Docker Compose #############

- docker compose up --build

The application will start on: http://localhost:8080
##############################################################

##################### Running Tests ##########################

mvn test
##############################################################

################### H2 Database access #######################

access via browser: http://localhost:8080/h2-console
with credentials:
			Driver class:	org.h2.Driver
			JDBC URL:	jdbc:h2:mem:piorfilme
			User Name:	as
			Password:	
##############################################################