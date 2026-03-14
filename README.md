# Backwarden – A REST-Compliant Password Manager API

## Description
Backwarden is a REST-compliant backend API for a password manager built with **Java** and **Quarkus**.  
The project serves as a demonstration of REST architecture and includes concepts such as **HATEOAS**, **pagination**, **caching strategies**, and **secure API design**.

## Documentation
Further details about the API design and usage can be found in the documentation:  
- [Backwarden Documentation](doc/BackwardenDocumentation.pdf)

## Build & Test
During the build process, the project automatically creates a **Docker image** of the API.  
Docker must therefore be running when executing the build.

To build the application and run all tests, navigate to the project directory and execute:

```bash
mvn clean verify
