# 🔗 URL Shortener API

[![Java Version](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

A production-oriented **REST API** built with **Spring Boot** for shortening URLs, tracking click analytics, and managing users — following industry-standard backend engineering practices.

---

## 🚦 Project Status
- ✅ **Functional Core:** URL shortening, redirection, custom shortcodes, expiration, and click analytics logging.
- 🚧 **Under Active Development:** Implementing JWT authentication, Redis caching, rate limiting, and Docker support.

---

## 💡 Why This Project?
This project was built to learn and demonstrate production-oriented Spring Boot development practices. It highlights structured layered architecture, DTO separation, robust input validation, centralized error handling, asynchronous database schedulers, and relational database schema design.

---

## ✨ Features

- 🔐 **User Registration** with BCrypt password hashing.
- ✂️ **URL Shortening** with auto-generated 6-character unique codes (collision-proof retry loops).
- ✏️ **Custom Shortcodes** with automatic uniqueness checks and whitespace trimming.
- ⏳ **URL Expiration (TTL)** with validation for past dates and automated daily database purging.
- 📊 **Click Analytics** — track IP address, user agent, and timestamp per click.
- 🛡️ **Input Validation** using Jakarta Bean Validation (`@NotBlank`, `@Email`, `@URL`, `@Future`).
- 📦 **Consistent API Responses** via a generic `ApiResponse<T>` wrapper.
- ⚠️ **Global Exception Handling** with custom exceptions mapped to clean REST responses.
- 🏛️ **Layered Architecture** — Controller → Service → Repository.

---

## 🛠️ Tech Stack

| Layer | Technology                       |
|---|----------------------------------|
| Language | Java 21                          |
| Framework | Spring Boot 4.1                  |
| ORM | Spring Data JPA / Hibernate      |
| Database | MySQL 8.0                        |
| Validation | Spring Boot Validation (Jakarta) |
| Security | Spring Security (BCrypt Hashing) |
| Utilities | Lombok                           |
| Build Tool | Maven                            |

---

## 📐 Architecture

### Layer & Request Flow Diagram

```text
POST /api/urls/shorten
        │
        ▼
Controller Layer (Validates input via @Valid UrlRequestDTO)
        │
        ▼
Service Layer (Executes business logic: custom check / auto-gen loop / TTL mapping)
        │
        ▼
Repository Layer (Spring Data JPA / DB Queries)
        │
        ▼
Database (MySQL)
```

### Directory Structure
```
src/main/java/com/example/URL_Shortener/
├── Config/              # Spring configuration (SecurityConfig with BCryptPasswordEncoder)
├── Controller/          # REST controllers — handles HTTP, delegates to services
├── DTO/
│   ├── requestDTO/      # Input payloads (UserRequestDTO, UrlRequestDTO, ClickRequestDTO)
│   └── responseDTO/     # Output payloads (UserResponseDTO, UrlResponseDTO, AnalyticsResponseDTO)
├── Exception/           # Centralized exception handlers & custom exception classes
├── Models/              # JPA Entities (User, Url, Click)
├── Repository/          # DB access layer (Spring Data JPA repositories)
└── Service/             # Core business logic (UserService, UrlService, ClickService)
```

---

## ⚙️ Design Decisions

### 👥 Why DTOs?
- **Data Encapsulation:** Prevents exposing the internal database entity structure directly to public API consumers.
- **Contract Decoupling:** Separates the API response structure from the physical database mappings.
- **Circular Reference Prevention:** Avoids infinite serialization loops when Jackson processes bidirectional relationships (`Url` <-> `Click`).

### 🔑 Why BCrypt Password Hashing?
- **Security by Design:** Ensures user passwords are never stored in plain text.
- **Rainbow Table Mitigation:** BCrypt automatically applies a random salt during hashing, defending against dictionary and pre-computed hash table attacks.

### 🗑️ Cascading vs. Scheduled Purging
- **Manual Deletes:** [Url.java](file:///src/main/java/com/example/URL_Shortener/Models/Url.java) defines `cascade = CascadeType.ALL, orphanRemoval = true` on the `clicks` relationship. Deleting a URL via the API automatically removes its click logs first.
- **Scheduled Purge:** Expired URLs are deleted daily at 2:00 AM using a Spring `@Scheduled` cron task. 
  - *Note:* Because JPQL bulk delete operations (`DELETE FROM Url u WHERE ...`) bypass JPA entity lifecycles, they do not trigger cascade removals. To prevent constraint violations, the cleanup task manually purges child click logs via a native SQL query with a JOIN before deleting the expired URLs.

---

## 🗄️ Database Schema & JPA Relationships

```
  ┌──────────┐              ┌──────────┐              ┌───────────┐
  │   User   │ 1          * │   Url    │ 1          * │   Click   │
  │ (Parent) ├─────────────>│ (Parent) ├─────────────>│  (Child)  │
  └──────────┘              └──────────┘              └───────────┘
```

- **User (1) ── (Many) Url:** A user can own multiple shortened URLs. Deleting a user will not automatically delete their URLs (designed for audit trails, unless configured).
- **Url (1) ── (Many) Click:** A single URL has multiple recorded click telemetry rows. If a URL is deleted manually, JPA cascades the deletion to remove all referencing clicks.

### Table Layout
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
├── url_id       BIGINT FK → urls.id ON DELETE CASCADE
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
The application reads database credentials from environment variables. Define the following in your system properties:
```bash
DB_URL=jdbc:mysql://localhost:3306/url_shortener
DB_USERNAME=root
DB_PASSWORD=your_password
```

Alternatively, you can configure them directly in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### 4. Build and Run
```bash
mvn spring-boot:run
```
The server starts by default at `http://localhost:8080`.

---

## 📡 API Reference

All JSON API endpoints return responses encapsulated in a consistent generic wrapper:
```json
{
  "success": true,
  "message": "Operation status description",
  "data": { ... }
}
```

---

### 👤 User Endpoints

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
    "createdAt": "2026-07-19T10:00:00"
  }
}
```

#### Get User by ID
```http
GET /api/users/{userId}
```

---

### 🔗 URL Endpoints

#### Shorten a URL
```http
POST /api/urls/shorten
Content-Type: application/json

{
  "userId": 1,
  "originalUrl": "https://www.example.com/very/long/path",
  "shortCode": "customAlias",       // Optional
  "expiresDate": "2026-12-31T23:59:59" // Optional future expiration date
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
    "shortCode": "customAlias",
    "createdDate": "2026-07-19T10:00:00",
    "expiresDate": "2026-12-31T23:59:59"
  }
}
```

#### Redirect via Shortcode (Access Redirection)
```http
GET /api/{shortCode}
```
> ⚠️ **Note:** This endpoint returns an HTTP `302 Found` redirection to the original URL. If the URL has expired (`expiresDate` is in the past), it yields an HTTP `404 Not Found` (planned update to `410 Gone`). It automatically records client click metrics (IP Address, User Agent, access time) in the background.

#### Get All URLs for a User
```http
GET /api/urls/user/{userId}
```

#### Delete a URL
```http
DELETE /api/urls/{urlId}
```

---

### 📊 Analytics Endpoints

#### Get Analytics for a URL
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
    "shortCode": "customAlias",
    "originalUrl": "https://www.example.com/very/long/path",
    "totalClicks": 3,
    "expiresDate": "2026-12-31T23:59:59",
    "isExpired": false,
    "clickDetails": [
      {
        "accessDate": "2026-07-19T10:05:00",
        "ipAddress": "192.168.1.1",
        "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
      }
    ]
  }
}
```

---

## ⚡ Quick Start with cURL

```bash
# 1. Register a new user
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"pass123"}'

# 2. Shorten a URL with a custom alias and expiration date
curl -X POST http://localhost:8080/api/urls/shorten \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"originalUrl":"https://www.google.com","shortCode":"ggl","expiresDate":"2026-12-31T23:59:59"}'

# 3. Access the short URL (records click analytics and follows redirection)
curl -L http://localhost:8080/api/ggl

# 4. View detailed click log analytics
curl http://localhost:8080/api/analytics/1
```

---

## 🛣️ Roadmap

### Implemented Features
- [x] User Registration with BCrypt password hashing.
- [x] Unique shortcode auto-generation (base-52 letter sets with collision checks).
- [x] Custom shortcode aliases with validation.
- [x] URL Expiration (TTL) checks during redirection.
- [x] Asynchronous daily cleanup of expired links via background `@Scheduled` job.
- [x] Analytics logging (IP, User-Agent, Access Time) and metrics aggregation endpoint.

### Planned Enhancements
- [ ] JWT authentication & authorization for endpoints.
- [ ] User login & session management endpoints.
- [ ] API documentation via Swagger UI / OpenAPI.
- [ ] Docker & Docker Compose containerization for rapid deployments.
- [ ] High-throughput performance scaling using Redis for URL redirect caching.
- [ ] Comprehensive unit and integration test suites.
- [ ] Rate limiting on creation APIs.

---

## 👤 Author
**Rajveer** — [GitHub Profile](https://github.com/rajveer1131)

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
