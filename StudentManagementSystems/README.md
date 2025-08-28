# Student Management System

A desktop application for managing student records, built with Java Swing and backed by an SQLite database. This project demonstrates skills in Java GUI development, event handling, and database connectivity (JDBC).

<img width="1478" height="886" alt="image" src="https://github.com/user-attachments/assets/135c5687-bc5a-4023-80a2-90e0861762d0" />


## Features

* **Full CRUD Operations:** Add, Update, and Delete student records.
* **Persistent Storage:** Student data is saved instantly to a local SQLite database (`students.db`).
* **Modern UI:**
    * Uses the system's native Look and Feel.
    * Sortable table by clicking on any column header.
    * Status bar for non-intrusive user feedback.
* **Data Validation:** Ensures that names (letters only) and age (realistic range) are valid before submission.
* **Flexible Data Entry:** Includes a dropdown for standard courses and an option to enter a custom course name.
* **Search Functionality:** Easily find students by name.

## How to Run

### Prerequisites

* Java Development Kit (JDK) 11 or higher.
* Eclipse IDE (or any other Java IDE).
* The [SQLite JDBC Driver](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/) `.jar` file.

### Setup in Eclipse

1.  Create a new Java project.
2.  Create the package `com.sms` inside the `src` folder.
3.  Add all four `.java` files to the `com.sms` package.
4.  Add the SQLite JDBC driver `.jar` to the project's build path:
    * Right-click the project -> **Build Path** -> **Configure Build Path...**
    * Go to the **Libraries** tab, select **Classpath**, and click **Add External JARs...**.
    * Select the downloaded `sqlite-jdbc-....jar` file.

5.  Run the `StudentManagementSystem.java` file. A `students.db` file will be created automatically in the project's root directory.
