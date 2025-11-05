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

## How to Run the Project (With or without Docker)


#### 1. Clone the Repository  

```
- git clone https://github.com/qtigerq/piorfilme-backend.git
```
```
- cd piorfilme-backend
```

### Without Docker

#### 2A. Install Dependencies and Build  
```
- mvn clean install
```
#### 3A. Run the Application
```
- mvn spring-boot:run
```
The application will start on: http://localhost:8080

### With docker

#### 2A. Run Application via Docker Compose  
```
- docker compose up --build
```
The application will start on: http://localhost:8080

#### Running Tests
```
- mvn test
```
##### H2 Database access

access via browser: http://localhost:8080/h2-console  
with credentials:  
			- Driver class: org.h2.Driver  
			- JDBC URL: jdbc:h2:mem:piorfilme  
			- User Name: as  
			- Password:  

---

## API Endpoints

Base URL:
```
http://localhost:8080
```

### List paginated movies (optional filters)
```
GET /movie?page=0&size=10&movieYear=1992&winner=true
```

**Example Response:**
```json
{
	"content": [
		{
			"id": 66,
			"movieYear": 1992,
			"title": "Shining Through",
			"studios": [
				{
					"id": 10,
					"name": "20th Century Fox"
				}
			],
			"producers": [
				{
					"id": 71,
					"name": "Howard Rosenman"
				},
				{
					"id": 70,
					"name": "Carol Baum"
				}
			],
			"winner": true
		}
	],
	"pageable": {
		"pageNumber": 0,
		"pageSize": 10,
		"sort": {
			"empty": true,
			"sorted": false,
			"unsorted": true
		},
		"offset": 0,
		"paged": true,
		"unpaged": false
	},
	"last": true,
	"totalPages": 1,
	"totalElements": 1,
	"size": 10,
	"number": 0,
	"sort": {
		"empty": true,
		"sorted": false,
		"unsorted": true
	},
	"first": true,
	"numberOfElements": 1,
	"empty": false
}
```

### Years with multiple winners
```
GET /movie/yearsWithMultipleWinners
```

**Example Response:**
```json
{
	"years": [
		{
			"year": 1986,
			"winnerCount": 2
		},
		{
			"year": 1990,
			"winnerCount": 2
		},
		{
			"year": 2015,
			"winnerCount": 2
		}
	]
}
```

### Studios ranked by win count
```
GET /movie/studiosWithWinCount
```
**Example Response:**
```json
{
	"studios": [
		{
			"name": "Universal Studios",
			"winCount": 2
		},
		{
			"name": "Associated Film Distribution",
			"winCount": 1
		},
		{
			"name": "C2 Pictures",
			"winCount": 1
		}
	]
}
```

### **Producers with minimum and maximum intervals between wins** (main challenge requirement)
```
GET /movie/maxMinWinIntervalForProducers
```

**Example Response:**
```json
{
  "min": [
    {
      "producer": "Producer A",
      "interval": 1,
      "previousWin": 2000,
      "followingWin": 2001
    }
  ],
  "max": [
    {
      "producer": "Producer B",
      "interval": 13,
      "previousWin": 1995,
      "followingWin": 2008
    }
  ]
}
```

### Winners by year:
GET /movie/winnersByYear?year=1990

**Example Response:**
```json
{
	"winners": [
		{
			"id": 56,
			"year": 1990,
			"title": "The Adventures of Ford Fairlane",
			"studios": [
				"20th Century Fox"
			],
			"producers": [
				"Steven Perry",
				"Joel Silver"
			],
			"winner": true
		},
		{
			"id": 57,
			"year": 1990,
			"title": "Ghosts Can't Do It",
			"studios": [
				"Triumph Releasing"
			],
			"producers": [
				"Bo Derek"
			],
			"winner": true
		}
	]
}
```