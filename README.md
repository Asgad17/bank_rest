# Bank Cards Management System

Backend REST API for managing bank cards, users and transfers.

The application is built with Java and Spring Boot and provides authentication, role-based authorization, card management and transfers between cards.

## Technologies

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- Maven
- Lombok
- Bean Validation
- OpenAPI / Swagger

## Features

### Authentication

- User registration
- User login
- JWT authentication
- Role-based authorization
- Roles: `USER` and `ADMIN`

### Cards

Users can:

- View their own cards
- View a card by ID
- Filter cards by status
- Use pagination

Administrators can:

- View all cards
- View cards of a specific user
- Create cards for users
- Block cards
- Activate cards
- Delete cards

### Transfers

Users can:

- Transfer money between their own cards
- View transfer history
- View a statement for a card

The application validates:

- Card ownership
- Transfer amount
- Card status
- Card balance
- Source and destination cards

## Project Structure

```text
src/main/java/com/example/bankcards
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── mapper
├── repository
├── security
└── service
```

## Database

The project uses PostgreSQL.

Database migrations are managed with Liquibase.

The migrations include:

- Users
- Cards
- Transfers
- Balance updates
- Database constraints
- Database indexes

## Configuration

Default database configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/bankcards
    username: ${DB_USERNAME:bankcards}
    password: ${DB_PASSWORD:bankcards}
```

Sensitive configuration should be provided through environment variables:

```text
DB_USERNAME
DB_PASSWORD
JWT_SECRET
JWT_EXPIRATION
```

## Running PostgreSQL

PostgreSQL can be started using Docker:

```bash
docker compose up -d postgres
```

Check the container:

```bash
docker ps
```

The application uses PostgreSQL on port `5433`.

## Running the Application

Make sure Java 21 and Maven are installed.

Run:

```bash
./mvnw spring-boot:run
```

Or run the application from IntelliJ IDEA using:

```text
BankcardsApplication
```

The API will be available at:

```text
http://localhost:8080
```

## API

### Authentication

Register:

```http
POST /api/auth/register
```

Example:

```json
{
  "username": "user1",
  "password": "12345678"
}
```

Login:

```http
POST /api/auth/login
```

Example:

```json
{
  "username": "user1",
  "password": "12345678"
}
```

The login endpoint returns a JWT token.

Use the token in protected requests:

```text
Authorization: Bearer <JWT_TOKEN>
```

### User Cards

Get current user's cards:

```http
GET /api/cards/my
```

Get card by ID:

```http
GET /api/cards/{cardId}
```

Filter by status:

```http
GET /api/cards/my?status=ACTIVE
```

Pagination:

```http
GET /api/cards/my?page=0&size=5
```

### Admin Cards

Get all cards:

```http
GET /api/cards/admin
```

Get user's cards:

```http
GET /api/cards/admin/user/{userId}
```

Create a card:

```http
POST /api/cards/admin/user/{userId}
```

Block a card:

```http
PATCH /api/cards/admin/{cardId}/block
```

Activate a card:

```http
PATCH /api/cards/admin/{cardId}/activate
```

Delete a card:

```http
DELETE /api/cards/admin/{cardId}
```

### Transfers

Create a transfer:

```http
POST /api/transfers
```

Example:

```json
{
  "fromCardId": 9,
  "toCardId": 10,
  "amount": 900
}
```

Get transfer statement:

```http
GET /api/transfers/{cardId}/statement
```

## Security

The application uses Spring Security with stateless JWT authentication.

Access is controlled by user roles:

| Operation | USER | ADMIN |
|---|:---:|:---:|
| Register | ✓ | ✓ |
| Login | ✓ | ✓ |
| View own cards | ✓ | - |
| View card by ID | ✓ | ✓ |
| View all cards | - | ✓ |
| Create card | - | ✓ |
| Block card | - | ✓ |
| Activate card | - | ✓ |
| Delete card | - | ✓ |
| Transfer money | ✓ | - |
| View own statement | ✓ | - |

## Validation and Error Handling

The application uses Bean Validation for request validation.

Global exception handling provides consistent error responses for:

- Card not found
- User not found
- Invalid transfer
- Access denied
- Invalid request data

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## Testing

The main API functionality was manually tested using Postman, including:

- Registration
- Login
- JWT authorization
- User card access
- Admin card management
- Card creation
- Card blocking
- Card activation
- Card deletion
- Transfers
- Transfer history
- Access restrictions
- Validation errors

## Author

**Asgad Niiazov**

Java Backend Developer

