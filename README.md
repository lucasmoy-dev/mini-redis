# ⚡ Mini Redis - Multi-Module Implementation

![Mini Redis Dashboard](mini_redis_dashboard_mockup_1770236327085.png)

A high-performance, professional implementation of a Redis-like service built with **Java** and **Hexagonal Architecture**. This project features a multi-module Maven structure, a RESTful API with Swagger, BDD Acceptance Tests, and a futuristic Interactive Dashboard.

## 🚀 Key Features

*   **Core Logic Engine**: Support for fundamental Redis commands (`SET`, `GET`, `DEL`, `INCR`, `DBSIZE`) and data structures like **Sorted Sets** (`ZADD`, `ZCARD`, `ZRANK`, `ZRANGE`).
*   **Hexagonal Architecture**: Clear separation between Domain, Application, and Infrastructure layers to ensure high maintainability and testability.
*   **RESTful API**: Exposes the Redis engine via Spring Boot with a prefix `/api` and full **Swagger/OpenAPI** documentation.
*   **Interactive Dashboard**: A premium **Glassmorphism** web interface with a real-time console, live stats, and a dramatic "Big Bang" introduction effect.
*   **Robust Parser**: Support for complex commands, including quoted strings with spaces (e.g., `SET key 'Hello World'`).
*   **Acceptance Testing**: End-to-end BDD tests using **Cucumber** and **RestAssured**.

## 🏗️ Project Structure

The project is divided into specialized Maven modules:

*   `mini-redis-core`: The pure Java domain and application logic. Zero dependencies on external frameworks (Hexagonal Core).
*   `mini-redis-server`: Spring Boot entry point, REST adapters, and Swagger configuration.
*   `mini-redis-tests`: BDD Acceptance Tests module that treats the system as a black box.
*   `mini-redis-dashboard`: Futuristic frontend built with HTML5/CSS3/VanillaJS, served directly by the server.

## 🛠️ Getting Started

### Prerequisites
*   Java 8 or higher
*   Maven 3.x (Optional, scripts provided for manual builds)

### Build and Run
1.  **Compile the whole project**:
    ```bash
    # Usage of provided batch scripts for environment compatibility
    compile_server.bat
    ```

2.  **Start the Server**:
    ```bash
    run_server.bat
    ```

3.  **Access the Dashboard**:
    Open your browser at:
    👉 [http://localhost:8080/index.html](http://localhost:8080/index.html)

4.  **Explore the API**:
    Documentation available at:
    👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🧪 Testing

To run the BDD acceptance tests, use the Cucumber runner located in the `mini-redis-tests` module. Alternatively, you can verify the system manually using the `run_acceptance_manual.bat` script.

---
*Built with ❤️ for the Mini Redis Challenge.*
