# bookstore
## Requirement
The user shall be able to list books, either the entire stock or by searching title/author.
Furthermore it shall be possible for the user to add and remove books from their basket(where the total price will be available).
It shall be possible to expand the bookstore with new items.
## Architecture
* Spring Boot: provide `RESTful` API for interaction
* Spring Shell: provide `command` for interaction
* JPA: Hibernate
* DB: embedded H2
## Execution
* command: `gradle bootRun`
* Directly run `Application.class` in IDE.
