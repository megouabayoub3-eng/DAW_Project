# demo08 (security-app)

Small Spring Boot demo application (RBAC / security) used for coursework.

## Features

- **User Authentication & Authorization**: Secure login system with role-based access control
- **Role Management**: ADMIN, USER roles with different access levels
- **Student Approval Workflow**: Students require approval before accessing certain features
- **Scholarship Management**: Students can apply for scholarships, teachers can review applications
- **Security Features**: Spring Security with proper authentication and authorization
- **Data Validation**: Comprehensive validation for all user inputs
- **Database Integration**: JPA/Hibernate with MySQL/H2 support

## Technologies Used

- Spring Boot 3.3.5
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL (with H2 for testing)
- Maven
- Java 17

## Build

From the project root run the included Maven wrapper:

```bash
./mvnw clean package
```

If you encounter interactive SSH prompts while Maven downloads plugins (e.g., from private repositories), run Maven in batch mode:

```bash
./mvnw -B clean package
```

Or disable interactive SSH for Git operations during the build:

```bash
GIT_SSH_COMMAND="ssh -o BatchMode=yes" ./mvnw -B test
```

## Run

Run the Spring Boot app:

```bash
./mvnw spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/security-app-0.0.1-SNAPSHOT.jar
```

## Tests

Run tests with:

```bash
./mvnw test
```

For non-interactive CI runs use batch mode:

```bash
./mvnw -B test
```

## Configuration

The application uses H2 in-memory database by default. To use MySQL, update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/security_app
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Default Users

- **Admin**: username: `admin`, password: `admin123`
- **Teacher**: username: `teacher`, password: `teacher123` 
- **Student**: username: `student`, password: `student123`

## API Endpoints

- `/login` - User login
- `/signup` - User registration
- `/admin/**` - Admin-only pages
- `/student/**` - Student-specific pages
- `/teacher/**` - Teacher-specific pages

## Notes / Next improvements

- Fix any failing tests discovered by `./mvnw test`.
- Improve logging and exception handling across services and controllers.
- Add a `CHANGELOG.md` and more project documentation.

If you want, I can run the test suite, fix failing tests, and continue improving code quality.
