# Warranty Tracker

A lightweight Spring Boot application designed to centralize consumer purchase records, manage warranty lifecycles, and automate expiration alerts via background cron workers.

---

## Core Capabilities

* **Automated Expiry Alerts:** Implements Spring Scheduling (`@Scheduled`) to execute daily cron tasks querying records reaching maturity within a 7-day window, triggering notifications via `JavaMailSender`.
* **Multipart Storage Engine:** Handles multi-format receipt uploads (`.png`, `.jpg`, `.webp`) with custom disk-persistence mapping and inline image serving endpoints.
* **Dynamic Search & Filtration:** Custom JPA queries handling real-time keyword lookups across product names, categories, and warranty states (Active, Expiring Soon, Expired).
* **Database Versioning:** Integrated PostgreSQL with Flyway database migration scripts ensuring repeatable schema versioning across deployment environments.
* **Dashboard UI:** Server-rendered interface built with Thymeleaf and responsive CSS layouts.

---

## Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3, Spring Data JPA, Spring Mail |
| **Database & Migration** | PostgreSQL, Flyway |
| **Frontend** | Thymeleaf, HTML5, CSS3 |
| **Build & Tooling** | Maven |

---

## Database Schema (PostgreSQL)

```sql
CREATE TABLE receipts (
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    purchase_date DATE NOT NULL,
    warranty_months INT NOT NULL,
    expiry_date DATE NOT NULL,
    receipt_img_url VARCHAR(255)
);
Local Setup & Configuration1. PrerequisitesJava 17+PostgreSQL 14+Maven2. Configure application.propertiesUpdate src/main/resources/application.properties with your database and mail credentials:Properties# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/warranty_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# SMTP Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Notification Target
app.mail.alert-recipient=your-email@gmail.com
3. Build & RunBash./mvnw clean install
./mvnw spring-boot:run
Access the dashboard at http://localhost:8080.EndpointsMethodEndpointDescriptionGET/Loads dashboard with metrics and active recordsPOST/saveReceiptProcesses multipart upload and creates receiptGET/showFormForUpdate/{id}Fetches receipt details for editingGET/delete/{id}Deletes record by ID and redirectsGET/uploads/{filename}Inline image retrieval endpoint for stored receipts
---

