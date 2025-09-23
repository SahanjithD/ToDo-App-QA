# Todo App Backend

This is a simple REST API backend for a Todo application built with Spring Boot.

## API Endpoints

- `POST /tasks` - Create a new task
- `GET /tasks` - List all tasks
- `PUT /tasks/{id}/complete` - Mark a task as complete
- `DELETE /tasks/{id}` - Delete a task

## Getting Started

1. Clone the repository
2. Run the application: `./mvnw spring-boot:run`
3. Access the API at: `http://localhost:8080`

## H2 Database Console

The H2 in-memory database console is available at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:todo`
- Username: `sa`
- Password: ` ` (empty)

## Example Usage

### Create a new task
