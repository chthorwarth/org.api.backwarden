# Backwarden – A REST-Compliant Password Manager API

## Description
A REST-compliant backend API for a password manager built with **Java** and **Quarkus**, following **Hexagonal Architecture**. The project demonstrates key **REST** concepts such as HATEOAS, pagination, and caching strategies, as well as key **security** concepts using secure password hashing and deterministic key derivation.

## Documentation
Further details about the API design and usage can be found in the documentation:  
- [Backwarden Documentation](doc/BackwardenDocumentation.pdf)

## Build & Test
During the build process, the project automatically creates a **Docker image** of the API.  
Docker must therefore be running when executing the build.

To build the application and run all tests, navigate to the project directory and execute:

```bash
mvn clean verify
