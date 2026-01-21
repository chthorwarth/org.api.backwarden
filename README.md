# Backend Systems – Portfolio 03

## Backwarden - Password Manager

### Build & Test 

**Important:** Please ensure Docker is running before executing this command.

To build the application and execute all tests, move to the project directory and run the following commands.

```bash
mvn clean verify

docker run -p 8085:8085 --name backwarden-api org.backwarden/api:1.0.0