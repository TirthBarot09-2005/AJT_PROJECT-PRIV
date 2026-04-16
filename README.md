# Advanced Java Library Management System

A modern Desktop application built with **Java Swing**, styling by **FlatLaf**, and **SQLite** database integration.

## Zero Setup Needed!
We have completely removed **XAMPP and MySQL**. The database is now powered by **SQLite**. 

This means **you don't have to start any servers or create any databases manually**. The application will automatically generate a file called `library.db` directly inside your project folder the first time you run it.

### Running the Application

1. **IDE Method (Recommended)**:
   - Open the source code folder in your IDE (IntelliJ, Eclipse, VS Code).
   - Let Maven sync the `pom.xml` dependencies.
   - Click Run on the `src/main/java/Main.java` class.

2. **Command Line Method**:
   - Ensure you have Maven installed.
   - Run `mvn clean install` inside the project folder to build.
   - Run `java -jar target/LibraryManagementSystem-1.0-SNAPSHOT-jar-with-dependencies.jar`

## Default Credentials
- **Username**: `admin`
- **Password**: `passwordx`
