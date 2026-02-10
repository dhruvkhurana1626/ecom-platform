# Swiggato Backend – Spring Boot REST APIs

This project is a backend implementation of a Swiggy-like food ordering system built using **Spring Boot**.  
It exposes RESTful APIs for managing customers, sellers, products, addresses, orders, and reviews.  
All APIs are documented and tested using **Swagger UI**, without any frontend dependency.

---

## Tech Stack

- Java  
- Spring Boot  
- Spring MVC  
- Spring Data JPA (Hibernate)  
- REST APIs  
- MySQL  
- Swagger (OpenAPI)  
- JavaMail Sender (Email Notifications)

---

## Modules & APIs

### 1. Customer Module
- Register a new customer
- Fetch customer by ID
- Fetch customers by gender or age
- **Email notification sent on successful registration**

### 2. Seller Module
- Onboard sellers/restaurants into the system

### 3. Product Module
- Add products for a seller
- Fetch products by category

### 4. Address Module
- Add address for a customer
- Update customer address
- Delete customer address

### 5. Order Module
- Place an order for a customer
- Accepts multiple order items
- **Email notification sent on successful order placement**

### 6. Review Module
- Add reviews for products
- Fetch reviews by customer or keyword

---

## Email Notifications

The application sends transactional emails for:
- Customer registration confirmation
- Order placement confirmation

Email logic is implemented at the **service layer** using a mail sender utility.

---

## API Documentation (Swagger)

All APIs are documented using Swagger UI.

After running the application, access Swagger at:

