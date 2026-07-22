# 📄 116FZ Document Generator

## Overview

116FZ Document Generator is a full-stack web application designed to automate the management of hazardous industrial facilities (HIF) and the generation of documentation required under the Russian Federal Law No. 116-FZ.

The project is built using a modern Spring Boot + React architecture and provides a secure REST API, user authentication, reference data management, image storage, and automatic Microsoft Word document generation.

---

# Features

## User Management

- User registration
- Authentication using Spring Security
- Password encryption with BCrypt
- Role-based authorization
- Protected REST API

## Hazardous Industrial Facilities

- Create, edit and delete industrial facilities
- View facility information
- Manage facility parameters
- Upload and manage facility images

## Organizations

- Full CRUD operations
- Validation
- Search and filtering support

## Emergency Response Units (ASF)

- CRUD operations
- Organization binding
- Reference data management

## Reference Data

The application provides management of reference information including:

- Organizations
- Emergency Response Units (ASF)
- Hazardous substances
- Facility types
- Cities
- Other reference entities

## Image Management

- Image upload
- Image preview
- Image deletion
- Image captions
- External links
- Automatic cache refresh after image updates

## Document Generation

Automatic Microsoft Word document generation based on templates.

Implemented features include:

- Placeholder replacement
- Table generation
- Image insertion
- Dynamic content generation
- Template-based architecture

---

# Architecture

The project follows a layered architecture.

```
React + TypeScript
        │
 REST API
        │
Spring Boot
        │
Controllers
        │
Services
        │
Repositories
        │
PostgreSQL
```

Backend layers:

```
controller
service
repository
entity
mapper
dto
 ├── request
 └── response
security
config
word
```

This architecture separates business logic, persistence, presentation, and API contracts, making the application maintainable and scalable.

---

# Technology Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate 6
- Maven
- REST API

## Frontend

- React
- TypeScript
- Material UI

## Database

- PostgreSQL

## Document Processing

- Microsoft Word template generation

## Infrastructure

- Docker
- Docker Compose

---

# Security

The application uses Spring Security to protect REST endpoints.

Implemented security features:

- Authentication
- Authorization
- Password hashing using BCrypt
- Session-based authentication
- Protected API endpoints

---

# REST API

The backend exposes a REST API consumed by the React frontend.

Examples of supported operations:

- User authentication
- Organization management
- Facility management
- ASF management
- Image upload
- Reference data retrieval
- Document generation

---

# Validation

The application validates incoming data before processing.

Validation includes:

- Required fields
- Password validation
- Duplicate login checking
- Entity consistency
- Request validation using DTOs

---

# Database

The application uses PostgreSQL together with Spring Data JPA and Hibernate.

Database features include:

- Entity relationships
- Foreign keys
- Lazy loading
- Repository abstraction
- Transaction management

---

# Docker Support

The project is fully containerized.

Docker is used for:

- Application packaging
- PostgreSQL deployment
- Local development
- Production deployment

---

# Project Structure

```
backend
│
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── mapper
├── repository
├── security
├── service
└── word

frontend
│
├── src
│   ├── api
│   ├── components
│   ├── pages
│   ├── hooks
│   ├── layouts
│   └── utils
```

---

# Running the Project

## Requirements

- Java 21
- Maven
- Node.js
- PostgreSQL
- Docker (optional)

## Backend

```bash
mvn clean package

mvn spring-boot:run
```

## Frontend

```bash
npm install

npm run dev
```

---

# Screenshots

## Login

*(Add screenshot)*

---

## Dashboard

*(Add screenshot)*

---

## Organization Management

*(Add screenshot)*

---

## Object Management

*(Add screenshot)*

---

## Image Management

*(Add screenshot)*

---

# Future Improvements

The project continues to evolve.

Planned improvements include:

- Spring Boot Actuator
- Application metrics
- Request logging
- Unit testing
- Integration testing
- CI/CD pipeline
- Monitoring with Prometheus and Grafana

---

# Author

Developed as a full-stack Java application using Spring Boot, React and PostgreSQL as part of a software engineering training project.