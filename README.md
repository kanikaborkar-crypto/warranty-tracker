#  Warranty Tracker — Full-Stack Cloud Application

A full-stack web application designed to track product warranties, manage receipt scans, and send automated expiration reminder emails before coverage lapses.

 **Live Deployment:** [https://warranty-tracker-wptf.onrender.com](https://warranty-tracker-wptf.onrender.com)

---

##  Key Features

* **Receipt & Warranty Management:** Full CRUD operations to track products, purchase dates, warranty duration, and categories.
* **Receipt Image Uploads:** Multipart file handling with dynamic modal lightbox preview.
* **Automated Email Alerts:** Background daemon utilizing Spring `@Scheduled` cron jobs to dispatch warning notifications 30 days prior to warranty expiration.
* **Visual Expiry Engine:** Dynamic progress bar showing real-time elapsed warranty duration.
* **Cloud Database Persistence:** Integrated with a managed cloud PostgreSQL database on Neon.
* **Containerized Architecture:** Fully dockerized for multi-stage production builds and deployed on Render.

---

## Architecture & Tech Stack

* **Backend:** Java 17+, Spring Boot 3 (Spring MVC, Spring Data JPA, Spring Mail, Spring Scheduling)
* **Frontend:** Thymeleaf, Tailwind CSS, JavaScript (Vanilla modal interactions)
* **Database:** PostgreSQL (Neon Cloud) / HikariCP Connection Pooling
* **Containerization & CI/CD:** Docker (Multi-stage build), Render Cloud Hosting

---

##  Environment Variables Configuration

To run this project locally or in production, configure the following environment variables:

| Variable | Description |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC connection URL (`jdbc:postgresql://...`) |
| `SPRING_DATASOURCE_USERNAME` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `MAIL_USERNAME` | Gmail address for automated email sender |
| `MAIL_PASSWORD` | Gmail 16-character App Password |
| `ALERT_RECIPIENT` | Target recipient email for expiration warnings |

---

##  Local Setup & Installation

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/kanikaborkar-crypto/warranty-tracker.git](https://github.com/kanikaborkar-crypto/warranty-tracker.git)
   cd warranty-tracker
Configure Database & Credentials:
Set the required properties in src/main/resources/application.properties or define your environment variables.

Build and Run with Maven:

Bash


./mvnw clean spring-boot:run
Run with Docker:

Bash


docker build -t warranty-tracker .
docker run -p 8080:8080 warranty-tracker
Access Application:
Navigate to http://localhost:8080 in your web browser.