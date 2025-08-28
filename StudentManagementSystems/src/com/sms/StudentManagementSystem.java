package com.sms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class StudentManagementSystem extends JFrame {
    // Backend service layer
    private final StudentService service;

    // UI Components
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField idField, nameField, ageField, searchField;
    private final JComboBox<String> courseComboBox;
    private final JButton addButton, updateButton, deleteButton, clearButton, searchButton;
    private final JLabel statusBar;

    public StudentManagementSystem() {
        // 1. Initialize the database
        DatabaseHandler.init();
        
        // 2. Initialize the service layer
        service = new StudentService();

        // 3. Setup the main window
        setTitle("Student Management System (Database Mode)");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- North Panel: Form for Data Entry ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        
        // --- CHANGE: Add "Custom..." to the list of courses ---
        String[] courses = {"Computer Science", "Physics", "Mathematics", "Electrical Engineering", "Biology", "Custom..."};
        courseComboBox = new JComboBox<>(courses);

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseComboBox);
        
        clearButton = new JButton("Clear Fields");
        formPanel.add(clearButton);

        // --- Center Panel: Table ---
        String[] columnNames = {"ID", "Name", "Age", "Course"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);

        // --- South Panel: Search and Status Bar ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        searchField = new JTextField(20);
        searchButton = new JButton("Search by Name");
        searchPanel.add(new JLabel("Enter Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        statusBar = new JLabel("Welcome! Connecting to database...");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(searchPanel, BorderLayout.CENTER);
        southPanel.add(statusBar, BorderLayout.SOUTH);
        
        refreshTable();

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFieldsFromSelectedRow();
            }
        });

        // --- West Panel: Buttons ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = createStyledButton("Add Student");
        updateButton = createStyledButton("Update Student");
        deleteButton = createStyledButton("Delete Student");
        
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(deleteButton);

        // --- Assemble Panels ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // --- Add Action Listeners ---
        setupActionListeners();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void setupActionListeners() {
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearFields());
        searchButton.addActionListener(e -> searchStudent());
    }

    private void refreshTable() {
        model.setRowCount(0);
        ArrayList<Student> students = service.getStudents();
        for (Student s : students) {
            model.addRow(new Object[]{s.getId(), s.getName(), s.getAge(), s.getCourse()});
        }
        statusBar.setText(students.size() + " students loaded from the database.");
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        courseComboBox.setSelectedIndex(0);
        searchField.setText("");
        table.clearSelection();
        idField.setEditable(true);
        statusBar.setText("Fields cleared.");
    }
    
    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            
            idField.setText(model.getValueAt(modelRow, 0).toString());
            nameField.setText(model.getValueAt(modelRow, 1).toString());
            ageField.setText(model.getValueAt(modelRow, 2).toString());

            // --- CHANGE: Handle displaying custom courses correctly ---
            String studentCourse = model.getValueAt(modelRow, 3).toString();
            DefaultComboBoxModel<String> comboModel = (DefaultComboBoxModel<String>) courseComboBox.getModel();
            // If the student's course isn't in the default list, add it temporarily for display
            if (comboModel.getIndexOf(studentCourse) == -1) {
                comboModel.addElement(studentCourse);
            }
            courseComboBox.setSelectedItem(studentCourse);
            // --- END CHANGE ---

            idField.setEditable(false);
            statusBar.setText("Selected student: " + nameField.getText());
        }
    }
    
    // --- CHANGE: New helper method to handle custom course input ---
    private String getCourseFromComboBox() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        if ("Custom...".equals(selectedCourse)) {
            String customCourse = JOptionPane.showInputDialog(this, "Enter custom course name:", "Custom Course", JOptionPane.PLAIN_MESSAGE);
            // If user cancels or enters empty string, return null to cancel operation
            if (customCourse == null || customCourse.trim().isEmpty()) {
                return null; 
            }
            return customCourse.trim();
        }
        return selectedCourse;
    }
    // --- END CHANGE ---

    private void addStudent() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            
            // --- CHANGE: Use the new helper method ---
            String course = getCourseFromComboBox();
            if (course == null) {
                statusBar.setText("Add student operation cancelled.");
                return; // Abort if user cancelled custom course input
            }
            // --- END CHANGE ---

            if (!name.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(this, "Name must contain only letters and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (age < 10 || age > 100) {
                JOptionPane.showMessageDialog(this, "Age must be between 10 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (service.searchStudentById(id) != null) {
                JOptionPane.showMessageDialog(this, "A student with this ID already exists.", "Add Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(id, name, age, course);
            service.addStudent(student);
            refreshTable();
            clearFields();
            statusBar.setText("Student '" + name + "' added to the database!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID and Age must be valid integers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Update Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            
            // --- CHANGE: Use the new helper method ---
            String course = getCourseFromComboBox();
            if (course == null) {
                statusBar.setText("Update student operation cancelled.");
                return; // Abort if user cancelled custom course input
            }
            // --- END CHANGE ---

            if (!name.matches("[a-zA-Z\\s]+")) {
                JOptionPane.showMessageDialog(this, "Name must contain only letters and spaces.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (age < 10 || age > 100) {
                JOptionPane.showMessageDialog(this, "Age must be between 10 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            service.updateStudent(id, name, age, course);
            refreshTable();
            clearFields();
            statusBar.setText("Student ID " + id + " updated in the database!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Delete Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        int id = (int) model.getValueAt(modelRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to permanently delete student with ID: " + id + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteStudent(id);
            refreshTable();
            clearFields();
        }
    }
    
    private void searchStudent() {
        String name = searchField.getText();
        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Search Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Student s = service.searchStudentByName(name);
        if (s != null) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(s.getId())) {
                    int viewRow = table.convertRowIndexToView(i);
                    table.setRowSelectionInterval(viewRow, viewRow);
                    table.scrollRectToVisible(table.getCellRect(viewRow, 0, true));
                    populateFieldsFromSelectedRow();
                    statusBar.setText("Student '" + name + "' found.");
                    return;
                }
            }
        }
        statusBar.setText("Student '" + name + "' not found.");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new StudentManagementSystem().setVisible(true));
    }
}