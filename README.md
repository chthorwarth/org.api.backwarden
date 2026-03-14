# Backwarden – Password Management API

## About the Project

This project was developed as a team effort during the Backend Systems module at the Technical University of Applied Sciences Würzburg-Schweinfurt (THWS). It’s a RESTful API for managing user credentials, inspired by password managers like Bitwarden/Backwarden.

### Motivation
We chose this project out of fascination for password managers and to explore the security and encryption concepts behind them. It also gave us hands-on experience with **Hexagonal Architecture** for decoupling and code reuse, and with **HATEOAS** to make the API self-descriptive and easy to explore. Implementing these concepts ourselves highlighted the practical benefits of modern backend design.


## Key Features
* **Hexagonal Architecture:** Decouples domain logic from infrastructure (Quarkus, Hibernate).
* **RESTful Principles:** Full hypermedia support (HATEOAS) for API discoverability and self-descriptive messages.
* **Security Standards:** Implementation of password hashing (bcrypt) and credential encryption (AES).
* **Concurrency & Caching:** Use of ETags to manage resource versions and prevent lost updates.

## Tech Stack
* **Language:** Java
* **Framework:** Quarkus (RESTEasy)
* **Persistence:** Hibernate JPA with H2 Database
* **Build & DevOps:** Maven, Docker

## Documentation
Detailed information regarding the architecture, API desgign, Security, and testing strategy can be found in the technical documentation:
- [Backwarden Documentation](doc/BackwardenDocumentation.pdf)
- [OpenAPI](https://app.swaggerhub.com/apis/thws/Backwaren/1.0.0)

## Build & Run
The project uses Maven and automatically creates a Docker image during the build process.

**Requirement:** Docker must be running.

```bash
# Build the project and run unit and integration tests
mvn clean verify
