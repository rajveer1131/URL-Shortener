# URL Shortener API

A Spring Boot REST API for shortening URLs and tracking analytics with user management.

## Features

- ✅ Create short URLs with unique codes
- ✅ Redirect to original URLs
- ✅ Track click analytics (IP, user agent, timestamp)
- ✅ User registration and management
- ✅ RESTful API design

## Tech Stack

- **Backend**: Java 11+, Spring Boot 4.1
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Dependencies**: Lombok, Spring Web, Spring Data JPA

## Prerequisites

- Java 11 or higher
- MySQL 8.0
- Maven 3.6+

## Setup

### 1. Database Setup

```bash
# Create database
mysql -u root -p
CREATE DATABASE url_shortener;
EXIT;
```

### 2. Clone & Configure

```bash
git clone https://github.com/rajveer1131/URL-Shortener.git
cd URL-Shortener
```

### 3. Update Database Credentials

Copy example configuration:
```bash
cp application.properties.example application.properties
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### 4. Run Application

```bash
mvn spring-boot:run
```

Server runs on: `http://localhost:8080`

## API Endpoints

### Users
```bash
# Register user
POST /api/users/register
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}

# Get user
GET /api/users/{userId}
```

### URLs
```bash
# Shorten URL
POST /api/urls/shorten
{
  "userId": 1,
  "originalUrl": "https://example.com/very/long/url"
}
Response: "abc123" (short code)

# Redirect
GET /api/abc123

# Get user's URLs
GET /api/urls/user/{userId}

# Delete URL
DELETE /api/urls/{urlId}
```

### Analytics
```bash
# Get analytics for URL
GET /api/analytics/{urlId}
```

## Example Usage

```bash
# 1. Register user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "pass123"
  }'

# 2. Shorten URL
curl -X POST http://localhost:8080/api/urls/shorten \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "originalUrl": "https://www.google.com"
  }'
# Returns: "PCEGVW"

# 3. Record click
curl -X POST http://localhost:8080/api/clicks/record \
  -H "Content-Type: application/json" \
  -d '{
    "urlId": 1,
    "ipAddress": "192.168.1.1",
    "userAgent": "Mozilla/5.0"
  }'

# 4. View analytics
curl http://localhost:8080/api/analytics/1
```

## Project Structure
