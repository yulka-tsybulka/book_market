# book_market
Book Market - Online Bookstore Project
Description
Book Market is an online bookstore project developed using Spring Boot. It provides a platform for users to register, browse books conveniently based on various parameters and categories, create a shopping cart, and place orders.

## Technologies and Tools
The project utilizes the following technologies and tools:

Spring Boot
Spring Security
Bearer Token
Spring Data JPA
Swagger
Liquibase
Pagination
Sorting
Docker

## Controllers

**AuthenticationController:** Handles user registration and authentication.

**BookController:**
CRUD operations for books (create, retrieve all, retrieve by id, update, delete).
Retrieve books by author and title parameters.

**CategoryController:**
CRUD operations for categories (create, retrieve all, retrieve by id, update, delete).
Retrieve books by category.

**OrderController:**
CRUD operations for orders (create, retrieve, update status, retrieve all order items, retrieve order item by id).

**ShoppingCartController:**
Retrieve shopping cart.
Update quantity by cart item id.
Delete cart item.

## User Roles

The application supports two types of users:

**Admin:** Has access to all endpoints including create, update, and delete operations.

**User:** Has access only to their own shopping cart and orders. They do not have access to endpoints for create, update, and delete operations.
Access Control
Unauthenticated users have access only to the registration and login pages.
Additional Notes
Please make sure to configure Bearer Token properly for authentication and authorization.

## Data Storage

The project utilizes MySQL database for data storage. MySQL is a popular relational database management system that provides robust features for efficient data storage and retrieval.
![Tables in db](MySQL db.jpg)


## Testing

The project includes comprehensive tests for the `Book` and `Category` entities to ensure their functionality and reliability. These tests cover various aspects such as entity creation, retrieval, updating, and deletion, as well as any associated business logic.
![Tests coverage](Tests coverage.jpg)


## Feel free to contribute and enhance the project!

## Getting Started

To run the project locally, follow these steps:

Clone this repository.

Configure the database settings in application.properties file.

Build and run the project using Maven or your preferred IDE.

Enjoy shopping for your favorite books online!

## Postman Collection
This Postman collection contains a set of pre-defined requests for interacting with the Book Market API. It includes requests for user authentication, book management, category management, order management, and shopping cart management.

You can download the collection file and import it into your Postman workspace to easily test and explore the functionalities of the Book Market API.
[Download Postman Collection](book market.postman_collection.json)