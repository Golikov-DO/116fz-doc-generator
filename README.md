```md
# 📄 116FZ-Doc-Generator

### System for automated document generation under Federal Law 116-FZ

A web application for managing hazardous industrial facilities data and generating documentation automatically.

---

## 🚀 Tech Stack

- **Backend:** Java 21, Hibernate 6 (ORM)
- **Database:** PostgreSQL 18
- **Web Server:** Apache Tomcat 10.1
- **Infrastructure:** Docker, Docker Compose, Maven
- **Deployment:** Railway Cloud

---

## 🛠️ Features

- Management of hazardous industrial objects
- Reference data management:
  - Emergency response units (ASF)
  - Hazardous substances
  - Organizations
- Automatic Word document generation
- Template-based data injection
- Hibernate ORM integration
- Cloud-ready configuration via environment variables

---

## 📄 Document Generation

The system generates Word documents using:

- Pipeline-based processing
- Placeholder replacement strategies
- Block factories (tables, lists, images)

---

## 🧠 Architecture

The application follows a layered architecture:

- **web** – Servlets (controllers)
- **app** – Application bootstrap and DI context
- **domain**
  - model – Entities
  - repository – Interfaces
  - service – Business logic
- **infrastructure** – Hibernate implementation

Custom lightweight dependency injection is implemented via:

- `RepositoryContext`
- `InternalServices`
- `Bootstrap`

---

## 📦 Local Setup

### 1. Requirements

- Docker Desktop
- Maven 3.9+

---

### 2. Start database

```bash
docker-compose up -d
