
package vartak;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class updateEmployee {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fintrackdbs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;
    private JComboBox<String> employeeDropdown;
    private JButton searchBtn, updateBtn, clearBtn;
    
    // Current employee details (read-only)
    private JLabel[] currentLabels;
    
    // New employee details (editable)
    private JTextField[] newTextFields;
    private JDateChooser newDOBChooser, newJoiningDateChooser;
    private JComboBox<String> departmentComboBox, genderComboBox;
    
    private String currentEmployeeFullName;
    private String[] departments = {"Engineering", "Business", "IT", "HR"};
    private String[] genders = {"M", "F", "Other"};

    public updateEmployee() {
        mainPanel = createMainPanel();
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 700)); // Reduced height since we removed images
        
        initializeComponents();
        setupLayout();
        loadEmployeeNames();
    }

    private void loadEmployeeNames() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT firstname, middlename, lastname FROM addemployee")) {
            
            employeeDropdown.removeAllItems();
            employeeDropdown.addItem(""); // Empty default
            
            while (rs.next()) {
                String firstName = rs.getString("firstname");
                String middleName = rs.getString("middlename");
                String lastName = rs.getString("lastname");
                
                StringBuilder fullName = new StringBuilder(firstName);
                if (middleName != null && !middleName.isEmpty()) {
                    fullName.append(" ").append(middleName);
                }
                if (lastName != null && !lastName.isEmpty()) {
                    fullName.append(" ").append(lastName);
                }
                
                employeeDropdown.addItem(fullName.toString().trim());
            }
        } catch (SQLException e) {
            showError("Error loading employee names", e);
        }
    }

    private void showError(String message, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(mainPanel, message + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    private void updateEmployee() {
        if (currentEmployeeFullName == null || currentEmployeeFullName.isEmpty()) {
            showMessage("Please select an employee first");
            return;
        }
        
        // Validate required fields
        if (newTextFields[0].getText().trim().isEmpty() ||  // First name
            newTextFields[2].getText().trim().isEmpty() ||  // Last name
            newTextFields[6].getText().trim().isEmpty() ||  // Mobile
            newTextFields[7].getText().trim().isEmpty() ||  // Email
            departmentComboBox.getSelectedItem() == null ||
            newJoiningDateChooser.getDate() == null) {
            showMessage("Please fill all required fields");
            return;
        }
        
        String[] nameParts = currentEmployeeFullName.split(" ");
        String updateQuery = buildUpdateQuery(nameParts);
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            
            // Set new values
            int paramIndex = 1;
            stmt.setString(paramIndex++, newTextFields[0].getText().trim()); // firstname
            stmt.setString(paramIndex++, newTextFields[1].getText().trim().isEmpty() ? null : newTextFields[1].getText().trim()); // middlename
            stmt.setString(paramIndex++, newTextFields[2].getText().trim()); // lastname
            stmt.setDate(paramIndex++, newDOBChooser.getDate() != null ? 
                new java.sql.Date(newDOBChooser.getDate().getTime()) : null); // dob
            stmt.setString(paramIndex++, (String) genderComboBox.getSelectedItem()); // gender
            stmt.setString(paramIndex++, newTextFields[3].getText().trim()); // address
            stmt.setString(paramIndex++, newTextFields[4].getText().trim()); // state
            stmt.setString(paramIndex++, newTextFields[5].getText().trim()); // pincode
            stmt.setString(paramIndex++, newTextFields[6].getText().trim()); // mob
            stmt.setString(paramIndex++, newTextFields[7].getText().trim()); // mail
            stmt.setString(paramIndex++, (String) departmentComboBox.getSelectedItem()); // department
            stmt.setDate(paramIndex++, new java.sql.Date(newJoiningDateChooser.getDate().getTime())); // dateOfJoining
            
            // Set WHERE clause parameters
            stmt.setString(paramIndex++, nameParts[0]); // firstname
            if (nameParts.length > 1) stmt.setString(paramIndex++, nameParts[1]); // middlename
            if (nameParts.length > 2) stmt.setString(paramIndex, nameParts[2]); // lastname
            
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                showMessage("Employee updated successfully");
                loadEmployeeNames(); // Refresh dropdown
                loadEmployeeDetails(); // Refresh displayed data
            } else {
                showMessage("Update failed - employee not found");
            }
        } catch (SQLException e) {
            showError("Database error during update", e);
        }
    }

    private JLabel createLabelWithBorder() {
        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return label;
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBorder(BorderFactory.createTitledBorder("Update Employee Details"));
        p.setBackground(new Color(255, 255, 204));
        
        // Calculate required height based on your last component position
        int lastComponentY = 650; // Reduced since we removed images
        int requiredHeight = lastComponentY + 50; // Add some padding
        
        p.setPreferredSize(new Dimension(800, requiredHeight));
        return p;
    }

    private String buildUpdateQuery(String[] nameParts) {
        StringBuilder query = new StringBuilder("UPDATE addemployee SET " +
            "firstname=?, middlename=?, lastname=?, dob=?, gender=?, " +
            "address=?, state=?, pincode=?, mob=?, mail=?, department=?, dateOfJoining=? " +
            "WHERE firstname=?");
        
        if (nameParts.length > 1) {
            query.append(" AND middlename=?");
        } else {
            query.append(" AND middlename IS NULL");
        }
        
        if (nameParts.length > 2) {
            query.append(" AND lastname=?");
        } else {
            query.append(" AND lastname IS NULL");
        }
        
        return query.toString();
    }

    private void setFieldsEditable(boolean editable) {
        for (JTextField field : newTextFields) {
            field.setEditable(editable);
        }
        newDOBChooser.setEnabled(editable);
        newJoiningDateChooser.setEnabled(editable);
        departmentComboBox.setEnabled(editable);
        genderComboBox.setEnabled(editable);
    }

    private void clearAllFields() {
        for (JLabel label : currentLabels) {
            label.setText("");
        }
        
        for (JTextField field : newTextFields) {
            field.setText("");
            field.setEditable(false);
        }
        
        newDOBChooser.setDate(null);
        newDOBChooser.setEnabled(false);
        newJoiningDateChooser.setDate(null);
        newJoiningDateChooser.setEnabled(false);
        departmentComboBox.setEnabled(false);
        genderComboBox.setEnabled(false);
        
        currentEmployeeFullName = null;
    }

    private String formatDate(Date date) {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "";
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(mainPanel, message);
    }

    private JTextField createTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        tf.setEditable(editable);
        return tf;
    }

    private void initializeComponents() {
        // Dropdown and search button
        employeeDropdown = new JComboBox<>();
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadEmployeeDetails());
        
        // Current employee labels
        String[] labelNames = {
            "First Name:", "Middle Name:", "Last Name:", "Date of Birth:", 
            "Age:", "Gender:", "Address:", "State:", "Pincode:", 
            "Mobile No:", "Email:", "Department:", "Joining Date:"
        };
        
        currentLabels = new JLabel[labelNames.length];
        for (int i = 0; i < currentLabels.length; i++) {
            currentLabels[i] = createLabelWithBorder();
        }
        
        // New employee fields
        newTextFields = new JTextField[9]; // First, Middle, Last, Address, State, Pincode, Mobile, Email, Age
        for (int i = 0; i < newTextFields.length; i++) {
            newTextFields[i] = createTextField(false);
        }
        
        // Dropdowns
        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setEnabled(false);
        
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setEnabled(false);
        
        // Date choosers
        newDOBChooser = new JDateChooser();
        newDOBChooser.setEnabled(false);
        newDOBChooser.addPropertyChangeListener("date", e -> calculateAge());
        
        newJoiningDateChooser = new JDateChooser();
        newJoiningDateChooser.setEnabled(false);
        
        // Action buttons
        updateBtn = new JButton("Update");
        updateBtn.addActionListener(e -> updateEmployee());
        
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearAllFields());
    }

    private void calculateAge() {
        if (newDOBChooser.getDate() != null) {
            long ageInMillis = System.currentTimeMillis() - newDOBChooser.getDate().getTime();
            int years = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
            newTextFields[8].setText(String.valueOf(years)); // Age field
        }
    }

    private void setupLayout() {
        mainPanel.removeAll();
        
        // Employee selection area
        mainPanel.add(new JLabel("Select Employee:")).setBounds(20, 20, 120, 25);
        mainPanel.add(employeeDropdown).setBounds(140, 20, 200, 25);
        mainPanel.add(searchBtn).setBounds(350, 20, 80, 25);

        // Current details section
        mainPanel.add(new JLabel("Current Details")).setBounds(20, 60, 150, 25);
        
        // Form layout for current details
        int y = 100; // Start lower since we removed the image
        String[] fieldLabels = {
            "First Name:", "Middle Name:", "Last Name:", "Date of Birth:", 
            "Age:", "Gender:", "Address:", "State:", "Pincode:", 
            "Mobile No:", "Email:", "Department:", "Joining Date:"
        };
        
        for (int i = 0; i < fieldLabels.length; i++) {
            mainPanel.add(new JLabel(fieldLabels[i])).setBounds(20, y, 100, 25);
            mainPanel.add(currentLabels[i]).setBounds(130, y, 200, 25);
            y += 30;
        }
        
        // New details section
        mainPanel.add(new JLabel("New Details")).setBounds(400, 60, 150, 25);
        
        // Form layout for new details
        y = 100; // Start at same height as current details
        for (int i = 0; i < 9; i++) { // Text fields
            String label = "";
            if (i == 0) label = "First Name:";
            else if (i == 1) label = "Middle Name:";
            else if (i == 2) label = "Last Name:";
            else if (i == 3) label = "Address:";
            else if (i == 4) label = "State:";
            else if (i == 5) label = "Pincode:";
            else if (i == 6) label = "Mobile No:";
            else if (i == 7) label = "Email:";
            else if (i == 8) label = "Age:";
            
            mainPanel.add(new JLabel(label)).setBounds(400, y, 100, 25);
            mainPanel.add(newTextFields[i]).setBounds(510, y, 200, 25);
            y += 30;
        }
        
        // Special fields
        mainPanel.add(new JLabel("Date of Birth:")).setBounds(400, y, 100, 25);
        mainPanel.add(newDOBChooser).setBounds(510, y, 200, 25);
        y += 30;
        
        mainPanel.add(new JLabel("Gender:")).setBounds(400, y, 100, 25);
        mainPanel.add(genderComboBox).setBounds(510, y, 200, 25);
        y += 30;
        
        mainPanel.add(new JLabel("Department:")).setBounds(400, y, 100, 25);
        mainPanel.add(departmentComboBox).setBounds(510, y, 200, 25);
        y += 30;
        
        mainPanel.add(new JLabel("Joining Date:")).setBounds(400, y, 100, 25);
        mainPanel.add(newJoiningDateChooser).setBounds(510, y, 200, 25);
        
        // Buttons at bottom
        mainPanel.add(clearBtn).setBounds(20, y + 40, 100, 30);
        mainPanel.add(updateBtn).setBounds(130, y + 40, 100, 30);
    }

    private void loadEmployeeDetails() {
        String selectedName = (String) employeeDropdown.getSelectedItem();
        if (selectedName == null || selectedName.isEmpty()) {
            clearAllFields();
            return;
        }
        
        currentEmployeeFullName = selectedName;
        String[] nameParts = selectedName.split(" ");
        
        String query = "SELECT * FROM addemployee WHERE firstname=? AND " + 
                      (nameParts.length > 1 ? "middlename=?" : "middlename IS NULL") + " AND " +
                      (nameParts.length > 2 ? "lastname=?" : "lastname IS NULL");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set parameters based on name parts
            stmt.setString(1, nameParts[0]);
            if (nameParts.length > 1) stmt.setString(2, nameParts[1]);
            if (nameParts.length > 2) stmt.setString(3, nameParts[2]);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Set current labels
                    currentLabels[0].setText(rs.getString("firstname"));
                    currentLabels[1].setText(rs.getString("middlename"));
                    currentLabels[2].setText(rs.getString("lastname"));
                    currentLabels[3].setText(formatDate(rs.getDate("dob")));
                    
                    // Calculate and display age
                    if (rs.getDate("dob") != null) {
                        long ageInMillis = System.currentTimeMillis() - rs.getDate("dob").getTime();
                        int years = (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
                        currentLabels[4].setText(String.valueOf(years));
                    }
                    
                    currentLabels[5].setText(rs.getString("gender"));
                    currentLabels[6].setText(rs.getString("address"));
                    currentLabels[7].setText(rs.getString("state"));
                    currentLabels[8].setText(rs.getString("pincode"));
                    currentLabels[9].setText(rs.getString("mob"));
                    currentLabels[10].setText(rs.getString("mail"));
                    currentLabels[11].setText(rs.getString("department"));
                    currentLabels[12].setText(formatDate(rs.getDate("dateOfJoining")));
                    
                    // Set editable fields
                    newTextFields[0].setText(rs.getString("firstname"));
                    newTextFields[1].setText(rs.getString("middlename"));
                    newTextFields[2].setText(rs.getString("lastname"));
                    newTextFields[3].setText(rs.getString("address"));
                    newTextFields[4].setText(rs.getString("state"));
                    newTextFields[5].setText(rs.getString("pincode"));
                    newTextFields[6].setText(rs.getString("mob"));
                    newTextFields[7].setText(rs.getString("mail"));
                    
                    if (rs.getDate("dob") != null) {
                        newDOBChooser.setDate(rs.getDate("dob"));
                        newTextFields[8].setText(currentLabels[4].getText()); // Age
                    }
                    
                    genderComboBox.setSelectedItem(rs.getString("gender"));
                    departmentComboBox.setSelectedItem(rs.getString("department"));
                    newJoiningDateChooser.setDate(rs.getDate("dateOfJoining"));
                    
                    // Enable all fields
                    setFieldsEditable(true);
                } else {
                    showMessage("Employee not found");
                    clearAllFields();
                }
            }
        } catch (SQLException e) {
            showError("Error loading employee details", e);
            clearAllFields();
        }
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}