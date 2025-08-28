package com.sms;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:students.db";
    public static void init() {
        String sql = """
                     CREATE TABLE IF NOT EXISTS students (
                         id INTEGER PRIMARY KEY,
                         name TEXT NOT NULL,
                         age INTEGER NOT NULL,
                         course TEXT NOT NULL
                     );
                     """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("course")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public static void addStudent(Student student) {
        String sql = "INSERT INTO students(id, name, age, course) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getCourse());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }
    
    public static void updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, age = ?, course = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setString(3, student.getCourse());
            pstmt.setInt(4, student.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    public static void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}