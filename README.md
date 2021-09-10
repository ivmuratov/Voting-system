# Voting system

### Description: 
The project has implemented a voting system. Two types of user:

* Regular user - can vote for the restaurant in which he wants to dine.
* Admin - can add, update, delete a restaurant, menu item, and another user.
Also, can vote for a restaurant.

### Technology stack:
Spring Boot 2.x, Lombok, JPA, H2, Hibernate, Jackson, Spring Security.

### The project was developed according to the following task:

## Task

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    * If it is before 11:00 we assume that he changed his mind.
    * If it is after 11:00 then it is too late, vote can't be changed
     
Each restaurant provides a new menu each day.

As a result, provide a link to github repository. 
It should contain the code, README.md with API documentation and couple curl commands to test it (better - Swagger).
***

[Link to REST API documentation (Swagger)](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

**Credentials:**
* Regular user
  * Username: `user@email.com`
  * Password: `user`
* Admin
  * Username: `admin@email.com`
  * Password: `admin`

