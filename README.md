# 🚀 Mini Redis - Java Implementation

A high-performance, thread-safe implementation of a Redis-like server in Java, designed with **Hexagonal Architecture** (Ports and Adapters) and built on **Spring Boot**.

## 🏗️ Architecture
This project follows **Domain-Driven Design (DDD)** and **Clean Architecture** principles:
- **`mini-redis-core`**: The heart of the application. Contains domain models, business logic (Services), and Port definitions. It is decoupled from infrastructure.
- **`mini-redis-server`**: Infrastructure layer. Implements the REST API, Command Line Interface (via Web), and Spring Boot configuration.
- **`mini-redis-tests`**: Integration and acceptance tests to ensure protocol compliance.

## 🛠️ Key Features
- **Thread-Safety**: Uses segmented locking (per-key mutex) for maximum concurrency.
- **Automated Expiration**: Supports TTL (Time To Live) with lazy cleanup.
- **Dual Interface**: Access via standard **Redis Commands** or a modern **REST API**.
- **Hexagonal Design**: Business logic is completely independent of the web framework.

## 🚀 Getting Started

### Prerequisites
- **JDK 11+** (Recommended) or JDK 8
- **Maven 3.8+**

### 1. Build and Install
Since this is a multi-module project, you must install the modules to your local repository first:
```bash
mvn clean install -DskipTests
```

### 2. Run the Server
```bash
mvn spring-boot:run -pl mini-redis-server
```

### 3. Access the Interactive Dashboard
Open your browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

### 4. Run Tests
```bash
# Run all tests (including Acceptance tests)
mvn test
```

## 📡 Networking & API Options

### Standard Command Interface
Execute any command via query parameter:
- `GET localhost:8080/api?cmd=SET user:1 Lucas`
- `GET localhost:8080/api?cmd=GET user:1`

### RESTful Interface
- **SET**: `PUT localhost:8080/api/{key}` (Body: value)
- **GET**: `GET localhost:8080/api/{key}`
- **DELETE**: `DELETE localhost:8080/api/{key}`

## ⚡ Supported Commands

| Category | Command | Description |
| :--- | :--- | :--- |
| **Strings** | `SET` `GET` `DEL` `INCR` | Basic key-value operations and atomic increment. |
| **Sorted Sets** | `ZADD` `ZCARD` `ZRANK` `ZRANGE` | Scoring-based sets with O(log N) operations. |
| **Database** | `DBSIZE` | Total keys count. |

## 🛡️ Input Constraints
To ensure compatibility, keys and values must match the pattern: `[a-zA-Z0-9-_:]`.
Non-existent keys return `(nil)`.

---
Developed by **Lucasmoy** - *Bringing high-performance Redis to the Java world.*
