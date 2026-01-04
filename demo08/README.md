# demo08 (security-app)

Small Spring Boot demo application (RBAC / security) used for coursework.

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

## Notes / Next improvements

- Fix any failing tests discovered by `./mvnw test`.
- Improve logging and exception handling across services and controllers.
- Add a `CHANGELOG.md` and more project documentation.

If you want, I can run the test suite, fix failing tests, and continue improving code quality.
