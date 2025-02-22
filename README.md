<hr></hr>
Overview
This JavaFX application manages courses, students, and grading. It connects to a MySQL database (school schema) to store and retrieve student records, course details, and grading results. The grading module provides different grading modes (Absolute, Relative, Normalised, etc.) and visualizes grade distribution through charts.  <hr></hr>
<hr></hr>
Project Structure
`src/Course_Selection`: Contains controllers for course selection interactions.
`src/Course_Selection/Add_Course`: Adds new courses associated with a professor.
`src/Course_Selection/Add_Students`: Adds new students (via CSV) to a selected course.
`src/Grading`: Contains controllers for grading logic and analytics (e.g., histograms, bar charts).
`src/database.sql`: Creates and populates the database schema used by the application.
<hr></hr>
How to Run Locally
Database Setup  
Install MySQL and create the `school` schema.
Run the SQL statements from `src/database.sql` to populate the tables.
Project Configuration  
Use IntelliJ IDEA (2024.3.3 or newer).
Open the project folder containing the `src` directory.
Ensure your `pom.xml` or project settings include the required dependencies for JavaFX and MySQL (e.g., MySQL JDBC driver).
Adjust Database Credentials  
In classes that connect to the database (e.g., `GradingController`, `CourseSelectionController`), replace existing JDBC URLs, usernames, and passwords with your own database details.
Build and Run  
In IntelliJ, click Run → Run..., choose the main class (if `Start.java` is the main entry point), and run the application.
Upon launching, log in or proceed to select courses, set grading modes, import students, and manage results.
Testing  
You can add or modify sample data in the `result`, `student`, or `course` tables as needed.
Run the UI and verify the grading functionality in each mode.
