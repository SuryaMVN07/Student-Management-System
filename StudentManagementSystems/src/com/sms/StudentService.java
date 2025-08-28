// File: StudentService.java
package com.sms;

import java.util.*;

public class StudentService {
    public ArrayList<Student> getStudents() {
        return DatabaseHandler.getAllStudents();
    }
    
    public void addStudent(Student student) {
        DatabaseHandler.addStudent(student);
    }
    
    public void updateStudent(int id, String name, int age, String course) {
        // Create a student object to pass to the handler
        Student student = new Student(id, name, age, course);
        DatabaseHandler.updateStudent(student);
    }
    
    public void deleteStudent(int id) {
        DatabaseHandler.deleteStudent(id);
    }
    
    public Student searchStudentById(int id) {
        for (Student s : getStudents()) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public Student searchStudentByName(String name) {
        for (Student s : getStudents()) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
    
    public ArrayList<Student> getStudentsSortedByName() {
        ArrayList<Student> students = getStudents();
        students.sort(Comparator.comparing(Student::getName));
        return students;
    }

    public ArrayList<Student> getStudentsSortedById() {
        ArrayList<Student> students = getStudents();
        students.sort(Comparator.comparingInt(Student::getId));
        return students;
    }
}