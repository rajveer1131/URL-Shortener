# 🔗 URL Shortener API

A production-ready **REST API** built with **Spring Boot** for shortening URLs, tracking click analytics, and managing users — following industry-standard backend engineering practices.

> Built to demonstrate real-world Spring Boot architecture: layered design, DTO pattern, global exception handling, input validation, and BCrypt password hashing.

---

## ✨ Features

- 🔐 **User Registration** with BCrypt password hashing
- ✂️ **URL Shortening** with auto-generated 6-character unique codes
- 📊 **Click Analytics** — track IP address, user agent, and timestamp per click
- 🛡️ **Input Validation** using Jakarta Bean Validation (`@NotBlank`, `@Email`, `@URL`)
- 📦 **Consistent API Responses** via a generic `ApiResponse<T>` wrapper
- ⚠️ **Global Exception Handling** with custom exceptions and clean error responses
- 🏛️ **Layered Architecture** — Controller → Service → Repository

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8.0 |
| Validation | Spring Boot Validation (Jakarta) |
| Security | Spring Security (BCrypt) |
| Boilerplate reduction | Lombok |
| Build Tool | Maven |

---

## 📐 Architecture

```
src/main/java/com/example/URL_Shortener/
├── Config/              # Spring configuration (SecurityConfig with BCryptPasswordEncoder)
├── Controller/          # REST controllers — handles HTTP, delegates to services
├── DTO/
│   ├── requestDTO/      # Input: UserRequestDTO, UrlRequestDTO, ClickRequestDTO
│   └── responseDTO/     # Output: UserResponseDTO, UrlResponseDTO, AnalyticsResponseDTO, ApiResponse<T>
├── Exception/           # GlobalExceptionHandler + custom exceptions
├── Models/              # JPA Entities: User, Url, Click
├── Repository/          # Spring Data JPA repositories
└── Service/             # Business logic: UserService, UrlService, ClickService
```

### Key Design Decisions

- **Entities are never exposed directly** — all responses go through DTOs to control the contract
- **No try/catch in controllers** — exceptions bubble up to `GlobalExceptionHandler` automatically
- **Password hashing in the Service layer**, not the controller — enforced regardless of entry point
- **Custom exceptions** (`ResourceNotFoundException`, `UserAlreadyExistsException`) map to correct HTTP status codes

---

## 🗄️ Database Schema

```
users
├── id           BIGINT PK AUTO_INCREMENT
├── username     VARCHAR NOT NULL
├── email        VARCHAR UNIQUE NOT NULL
├── password     VARCHAR NOT NULL (BCrypt hashed)
└── created_at   DATETIME

urls
├── id           BIGINT PK AUTO_INCREMENT
├── original_url VARCHAR NOT NULL
├── short_code   VARCHAR NOT NULL UNIQUE
├── user_id      BIGINT FK → users.id
├── created_date DATETIME
└── expires_date DATETIME (nullable)

clicks
├── id           BIGINT PK AUTO_INCREMENT
├── url_id       BIGINT FK → urls.id
├── access_date  DATETIME
├── ip_address   VARCHAR
└── user_agent   VARCHAR
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- MySQL 8.0+
- Maven 3.6+

### 1. Clone the Repository

```bash
git clone https://github.com/rajveer1131/URL-Shortener.git
cd URL-Shortener
```

### 2. Create the Database

```sql
CREATE DATABASE url_shortener;
```

### 3. Configure Environment Variables

The application reads credentials from environment variables. Set the following:

```bash
DB_URL=jdbc:mysql://localhost:3306/url_shortener
DB_USERNAME=root
DB_PASSWORD=your_password
```

Or update `src/main/resources/application.properties` directly:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### 4. Run

```bash
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## 📡 API Reference

All responses follow a consistent envelope:

```json
{
  "success": true,
  "message": "Description of result",
  "data": { ... }
}
```

---

### 👤 Users

#### Register User
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com",
  "password": "secret123"
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "User got saved successfully",
  "data": {
    "userId": 1,
    "username": "john",
    "email": "john@example.com",
    "createdAt": "2025-01-01T10:00:00"
  }
}
```

#### Get User by ID
```http
GET /api/users/{userId}
```

---

### 🔗 URLs

#### Shorten a URL
```http
POST /api/urls/shorten
Content-Type: application/json

{
  "userId": 1,
  "originalUrl": "https://www.example.com/very/long/path"
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "URL shortened successfully",
  "data": {
    "id": 1,
    "originalUrl": "https://www.example.com/very/long/path",
    "shortCode": "aBcDeF",
    "createdDate": "2025-01-01T10:00:00",
    "expiresDate": null
  }
}
```

#### Redirect via Short Code (Auto-records click)
```http
GET /api/urls/{shortCode}
```
> ⚠️ This endpoint returns a **302 redirect** to the original URL, not a JSON response. It also automatically records a click (IP address, user agent, timestamp).

#### Get All URLs for a User
```http
GET /api/urls/user/{userId}
```

#### Delete a URL
```http
DELETE /api/urls/{urlId}
```

---

### 📊 Analytics

#### Get Click Analytics for a URL
```http
GET /api/analytics/{urlId}
```

**Response** `200 OK`
```json
{
  "success": true,
  "message": "Successfully fetched Analytics",
  "data": {
    "urlId": 1,
    "shortCode": "aBcDeF",
    "originalUrl": "https://www.example.com/very/long/path",
    "totalClicks": 3,
    "clickDetails": [
      {
        "accessDate": "2025-01-01T10:05:00",
        "ipAddress": "192.168.1.1",
        "userAgent": "Mozilla/5.0"
      }
    ]
  }
}
```

---

## ⚡ Quick Start with cURL

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123"}'

# 2. Shorten a URL
curl -X POST http://localhost:8080/api/urls/shorten \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"originalUrl":"https://www.google.com"}'

# 3. Access the short URL (auto-records click, follows 302 redirect)
curl -L http://localhost:8080/api/urls/aBcDeF

# 4. View analytics
curl http://localhost:8080/api/analytics/1
```

---

## 🛣️ Roadmap

- [ ] JWT authentication & authorization
- [ ] User login endpoint
- [ ] URL expiration enforcement
- [ ] Custom short codes
- [ ] Rate limiting
- [ ] Docker & Docker Compose support
- [ ] Unit & integration tests

---

## 👤 Author

**Rajveer** — [GitHub](https://github.com/rajveer1131)

---

## 📄 License

MIT
