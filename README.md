# Peerspace Clone: Hourly Space Rental Platform

Welcome to the repository for the **Peerspace Clone**, a marketplace for short term venue rentals!

## 🚀 Overview

This platform connects **Hosts** (space owners) with **Guests** (tenants/event organizers). It handles the complete lifecycle of a space rental, from browsing and booking to messaging, payments, and post-event reviews. 



## 🛠️ Tech Stack

This project is built using a modern, scalable full-stack architecture:

* **Database:** **PostgreSQL**.
* **Backend API:** **Java & Spring Boot**
  * *Spring Web:* For RESTful API endpoints.
  * *Spring Data JPA / Hibernate:* For ORM and seamless database interactions.
  * *Spring Security:* For role-based access control (RBAC) and JWT authentication.
  * *Maven:* For build automation.
* **Frontend:** **React.js**
* **Package Manager:** **npm**

---
To compile the backend execute: ./mvnw compile
To run execute: ./mvnw spring-boot:run

## API Testing & Verification

This guide describes how to test all of the Peerspace/Airbnb backend endpoints on your local server.

> [!NOTE]
> **Active Port:** The server is configured to run on port **`8082`** locally. Ensure your URLs point to `http://localhost:8082`.

---

## 1. Authentication & JWT Setup (Crucial)

Since **Spring Security** is enabled, protected endpoints require a `Bearer Token`.

### Step 1: Register a new User
* **Method:** `POST`
* **URL:** `http://localhost:8082/api/v1/users` (Permitted / public)
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**
  ```json
  {
    "username": "guest_demo",
    "email": "guest_demo@example.com",
    "password": "password123",
    "firstName": "Guest",
    "lastName": "Demo",
    "roleIds": [3]
  }
  ```
  *(Note: Use `[3]` for `GUEST` role, or `[2]` for `HOST` role)*

* **Using `curl`:**
  ```bash
  curl -X POST http://localhost:8082/api/v1/users \
    -H "Content-Type: application/json" \
    -d '{"username":"guest_demo","email":"guest_demo@example.com","password":"password123","firstName":"Guest","lastName":"Demo","roleIds":[3]}'
  ```

### Step 2: Login to get a JWT
* **Method:** `POST`
* **URL:** `http://localhost:8082/api/v1/auth/login` (Permitted / public)
* **Headers:** `Content-Type: application/json`
* **Body (JSON):**
  ```json
  {
    "username": "guest_demo",
    "password": "password123"
  }
  ```
* **Using `curl`:**
  ```bash
  curl -X POST http://localhost:8082/api/v1/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"guest_demo","password":"password123"}'
  ```
* **Response:** Copy the `"token"` value from the response body.

### Step 3: Configure JWT in Postman
For all subsequent requests:
1. In Postman, go to the **Authorization** tab.
2. Select **Bearer Token** as the type.
3. Paste your token into the **Token** input field.

---

## 2. Testing Endpoints Step-by-Step

### 1. User Management
* **Register a User:** See registration details above.
* **Fetch User Details:** `GET http://localhost:8082/api/v1/users/{uuid}`
* **Update User:** `PUT http://localhost:8082/api/v1/users/{uuid}`
* **Delete User (Soft Delete):** `DELETE http://localhost:8082/api/v1/users/{uuid}`

---

### 2. Property / Space Listings
* **Create Property:** `POST http://localhost:8082/api/v1/properties`
  ```json
  {
    "hostId": 1,
    "title": "Industrial Warehouse Loft",
    "description": "Large creative loft space for photoshoots.",
    "city": "Athens",
    "address": "Ermou 45",
    "status": "APPROVED",
    "hourlyRate": 45.00,
    "halfDayRate": 160.00,
    "zip": "10556",
    "timezone": "Europe/Athens",
    "longitude": 23.7275,
    "latitude": 37.9838,
    "sizeSqm": 120,
    "capacity": 8,
    "minHours": 6,
    "maxHours": 24,
    "type": "PHOTO_STUDIO"
  }
  ```
* **Get Property by ID:** `GET http://localhost:8082/api/v1/properties/{id}`
* **Get Properties by Host:** `GET http://localhost:8082/api/v1/properties/host/{hostId}`

---

### 3. Bookings
* **Create Booking Request:** `POST http://localhost:8082/api/v1/bookings`
  ```json
  {
    "propertyId": 1,
    "guestId": 2,
    "startDatetime": "2026-09-01T10:00:00Z",
    "endDatetime": "2026-09-01T16:00:00Z"
  }
  ```
* **Complete Booking (For testing reviews):** `PUT http://localhost:8082/api/v1/bookings/{id}/complete`
  *(No request body needed. Changes status to `COMPLETED` so you can write a review).*
* **Cancel Booking:** `PUT http://localhost:8082/api/v1/bookings/{id}/cancel`
  ```json
  {
    "canceledBy": "GUEST",
    "cancelationReason": "Change of plans"
  }
  ```

---

### 4. Payments
* **Process Booking Payment:** `POST http://localhost:8082/api/v1/payments`
  ```json
  {
    "bookingId": 1,
    "amount": 225.00,
    "currency": "EUR",
    "method": "CREDIT_CARD"
  }
  ```
* **Refund Payment:** `POST http://localhost:8082/api/v1/payments/{id}/refund`
  ```json
  {
    "refundAmount": 225.00
  }
  ```

---

### 5. Messaging & Inbox
* **Send Message:** `POST http://localhost:8082/api/v1/messages`
  ```json
  {
    "bookingId": 1,
    "senderId": 2,
    "content": "Is there access to parking?"
  }
  ```
* **Fetch Conversation History:** `GET http://localhost:8082/api/v1/messages/booking/{bookingId}`

---

### 6. Reviews & Ratings
* **Create Review:** `POST http://localhost:8082/api/v1/reviews`
  ```json
  {
    "bookingId": 1,
    "reviewerId": 2,
    "rating": 5,
    "comment": "Perfect space, very clean and friendly host!",
    "isPublic": true
  }
  ```
* **Get Reviews for Property:** `GET http://localhost:8082/api/v1/reviews/property/{propertyId}`

---

### 7. Blocked Slots (Calendar blocks)
* **Block Date Range:** `POST http://localhost:8082/api/v1/blocked-slots`
  ```json
  {
    "propertyId": 1,
    "startTime": "2026-09-02T08:00:00",
    "endTime": "2026-09-02T18:00:00",
    "reason": "Host maintenance"
  }
  ```
* **Fetch Blocked Ranges:** `GET http://localhost:8082/api/v1/blocked-slots/property/{propertyId}`
