# Library system

A desktop app for running a library: books, members and loans. I built it for the Java course at TVZ (Zagreb University of Applied Sciences).

## Stack

- Java 25 (JPMS module)
- JavaFX 21 + FXML
- H2 (embedded) through JDBC
- JSON-B (Yasson) for JSON export and import, JAXB for the XML change log
- SLF4J + Logback
- Maven, JUnit 5

## What it does

- Librarians log in. Passwords are stored as PBKDF2 hashes, and after 3 failed attempts the login locks for 30 seconds.
- Librarians add and search books and members. To delete one, right-click its row in the table.
- Lending and returning a book updates the number of available copies in the same transaction.
- A backup copies the books table inside the database and exports all data to JSON and to a binary file (Java serialization).
- Data can be restored from the JSON copy.
- Every action is written to `log.xml`, and the app can show that history.
- Long operations (backup, loading) run on virtual threads.

## Running it

You need JDK 25.

```bash
./mvnw javafx:run
```

On Windows: `mvnw.cmd javafx:run`

On the first start the app creates the database in `database/` and fills it with a few sample records.

Demo login: **admin** / **admin123**

## Database settings

The app reads `src/main/resources/database.properties`. If that file is missing, it uses `database.properties.example` (a local H2 database in the project folder). For your own settings, copy the example:

```bash
cp src/main/resources/database.properties.example src/main/resources/database.properties
```

`database.properties` is in `.gitignore`, so it stays out of the repository.

## Structure

```
src/main/java
├── com/example/sistemska_knjiznica   controllers and app startup
├── entity                            model (Book, Member, Loan, Librarian...)
├── repository                        database access
├── utils                             database, files, validation, messages
└── exceptions                        custom exceptions
src/main/resources
├── schema.sql                        schema and sample data
└── com/example/sistemska_knjiznica   FXML screens and CSS
```

Files the app creates while it runs (`database/`, `backup/`, `logs/`, `log.xml`) are not in the repository.

## Tests

```bash
./mvnw test
```
