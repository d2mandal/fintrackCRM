// package vartak;

// import java.awt.Font;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import javax.swing.BorderFactory;
// import javax.swing.JButton;
// import javax.swing.JComboBox;
// import javax.swing.JLabel;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.JTextField;
// import javax.swing.SwingUtilities;
// import javax.swing.border.TitledBorder;
// import javax.swing.table.DefaultTableModel;

// public class ViewEmployee {

//     private final JPanel empPanel;
//     private JTable employeeTable;
//     private JTextField searchField;
//     private JComboBox<String> searchFilter;
//     private DefaultTableModel tableModel;

//     private JPanel detailsPanel;
//     private JTextField idField, nameField, deptField, joinDateField, mobileField, emailField;
//     private JButton backButton;

//     public ViewEmployee() {
//         empPanel = new JPanel(null);
//         Font titleFont = new Font("Arial", Font.BOLD, 16);
//         TitledBorder border = BorderFactory.createTitledBorder("View Employee");
//         border.setTitleFont(titleFont);
//         empPanel.setBorder(border);

//         JLabel searchLabel = new JLabel("Search:");
//         searchLabel.setBounds(20, 30, 60, 25);
//         empPanel.add(searchLabel);

//         searchField = new JTextField();
//         searchField.setBounds(90, 30, 150, 25);
//         empPanel.add(searchField);

//         String[] filters = {"Employee ID", "Name", "Department"};
//         searchFilter = new JComboBox<>(filters);
//         searchFilter.setBounds(250, 30, 120, 25);
//         empPanel.add(searchFilter);

//         JButton searchButton = new JButton("Search");
//         searchButton.setBounds(380, 30, 80, 25);
//         empPanel.add(searchButton);

//         // Employee Table
//         String[] columnNames = {"ID", "Name", "Department", "Joining Date", "Mobile Number", "Email"};
//         tableModel = new DefaultTableModel(columnNames, 0) {
//             @Override
//             public boolean isCellEditable(int row, int column) {
//                 return false;
//             }
//         };

//         employeeTable = new JTable(tableModel);
//         employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);
//         // Adjust column width for "ID" 
//         employeeTable.getColumnModel().getColumn(1).setPreferredWidth(400); // Adjust column width for "Name" 
//         employeeTable.getColumnModel().getColumn(2).setPreferredWidth(400); // Adjust column width for "Department" 
//         employeeTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Adjust column width for "Joining Date" 
//         employeeTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Adjust column width for "Mobile Number"
//         employeeTable.getColumnModel().getColumn(5).setPreferredWidth(500); // Adjust column width for "Gmail"
//         JScrollPane scrollPane = new JScrollPane(employeeTable);
//         scrollPane.setBounds(20, 70, 700, 250);
//         empPanel.add(scrollPane);

//         SwingUtilities.invokeLater(this::loadEmployeeDetails);

//         employeeTable.addMouseListener(new MouseAdapter() {
//             @Override
//             public void mouseClicked(MouseEvent e) {
//                 if (e.getClickCount() == 2) {
//                     int selectedRow = employeeTable.getSelectedRow();
//                     if (selectedRow != -1) {
//                         showEmployeeDetails(selectedRow);
//                     }
//                 }
//             }
//         });

//         setupDetailsPanel();

//         JButton viewButton = new JButton("View Details");
//         viewButton.setBounds(100, 340, 120, 30);
//         empPanel.add(viewButton);

//         JButton deleteButton = new JButton("Delete Employee");
//         deleteButton.setBounds(250, 340, 140, 30);
//         empPanel.add(deleteButton);

//         viewButton.addActionListener(e -> {
//             int selectedRow = employeeTable.getSelectedRow();
//             if (selectedRow != -1) {
//                 showEmployeeDetails(selectedRow);
//             }
//         });

       
//     }

//     public JPanel getPanel() {
//         return empPanel;
//     }

//     private Connection getConnection() {
//         try {
//             return DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
//         } catch (SQLException e) {
//             e.printStackTrace();
//             throw new RuntimeException("Unable to connect to the database. Please try again later.");
//         }
//     }

//     private void loadEmployeeDetails() {
//         String query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail FROM addemployee";

//         try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

//             tableModel.setRowCount(0);

//             while (rs.next()) {
//                 String fullName = rs.getString("firstname") + " " + rs.getString("middlename") + " " + rs.getString("lastname");
//                 tableModel.addRow(new Object[]{
//                     rs.getString("empId"),
//                     fullName,
//                     rs.getString("Department"),
//                     rs.getString("dateOfJoining"),
//                     rs.getString("mob"),
//                     rs.getString("mail")
//                 });
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void setupDetailsPanel() {
//         detailsPanel = new JPanel(null);
//         detailsPanel.setBounds(20, 70, 700, 250);
//         detailsPanel.setVisible(false);
//         empPanel.add(detailsPanel);

//         JLabel idLabel = new JLabel("ID:");
//         idLabel.setBounds(20, 20, 100, 25);
//         detailsPanel.add(idLabel);

//         idField = new JTextField();
//         idField.setBounds(150, 20, 200, 25);
//         idField.setEditable(false);
//         detailsPanel.add(idField);

//         JLabel nameLabel = new JLabel("Name:");
//         nameLabel.setBounds(20, 50, 100, 25);
//         detailsPanel.add(nameLabel);

//         nameField = new JTextField();
//         nameField.setBounds(150, 50, 200, 25);
//         nameField.setEditable(false);
//         detailsPanel.add(nameField);

//         JLabel deptLabel = new JLabel("Department:");
//         deptLabel.setBounds(20, 80, 100, 25);
//         detailsPanel.add(deptLabel);

//         deptField = new JTextField();
//         deptField.setBounds(150, 80, 200, 25);
//         deptField.setEditable(false);
//         detailsPanel.add(deptField);

//         JLabel joinDateLabel = new JLabel("Joining Date:");
//         joinDateLabel.setBounds(20, 110, 100, 25);
//         detailsPanel.add(joinDateLabel);

//         joinDateField = new JTextField();
//         joinDateField.setBounds(150, 110, 200, 25);
//         joinDateField.setEditable(false);
//         detailsPanel.add(joinDateField);

//         JLabel mobileLabel = new JLabel("Mobile:");
//         mobileLabel.setBounds(20, 140, 100, 25);
//         detailsPanel.add(mobileLabel);

//         mobileField = new JTextField();
//         mobileField.setBounds(150, 140, 200, 25);
//         mobileField.setEditable(false);
//         detailsPanel.add(mobileField);

//         JLabel emailLabel = new JLabel("Email:");
//         emailLabel.setBounds(20, 170, 100, 25);
//         detailsPanel.add(emailLabel);

//         emailField = new JTextField();
//         emailField.setBounds(150, 170, 200, 25);
//         emailField.setEditable(false);
//         detailsPanel.add(emailField);

//         backButton = new JButton("Back");
//         backButton.setBounds(150, 200, 150, 30);
//         detailsPanel.add(backButton);
//         backButton.addActionListener(e -> switchToTableView());
//     }

//     private void showEmployeeDetails(int selectedRow) {
//         if (selectedRow < 0 || selectedRow >= employeeTable.getRowCount()) {
//             javax.swing.JOptionPane.showMessageDialog(empPanel, "Please select a valid employee.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         // Get data from the selected row
//         idField.setText(employeeTable.getValueAt(selectedRow, 0).toString());
//         nameField.setText(employeeTable.getValueAt(selectedRow, 1).toString());
//         deptField.setText(employeeTable.getValueAt(selectedRow, 2).toString());
//         joinDateField.setText(employeeTable.getValueAt(selectedRow, 3).toString());
//         mobileField.setText(employeeTable.getValueAt(selectedRow, 4).toString());
//         emailField.setText(employeeTable.getValueAt(selectedRow, 5).toString());

//         // Switch Views
//         detailsPanel.setVisible(true);
//         employeeTable.setVisible(false);

//         // Ensure revalidation of the panel
//         empPanel.revalidate();
//         empPanel.repaint();
//     }

//     private void switchToTableView() {
//         detailsPanel.setVisible(false);
//         employeeTable.setVisible(true);

//         empPanel.revalidate();
//         empPanel.repaint();

//     }

//     // private void deleteEmployee(int selectedRow) {
//     //     String empId = employeeTable.getValueAt(selectedRow, 0).toString();
//     //     String query = "DELETE FROM addemployee WHERE empId = ?";
//     //     try (Connection conn = getConnection();
//     //          PreparedStatement stmt = conn.prep 
//     //         stmt.setString(1, empId);
//     //         int rowsDeleted = stmt.executeUpdate();
//     //         if (rowsDeleted > 0) {
//     //             tableModel.removeRow(selectedRow);
//     //             javax.swing.JOptionPane.showMessageDialog(empPanel, "Employee deleted successfully.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
//     //         }
//     //     } catch (SQLException e) {
//     //         e.printStackTrace();
//     //         javax.swing.JOptionPane.showMessageDialog(empPanel, "Error deleting employee.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
//     //     }
//     // }
// }


package vartak;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class ViewEmployee {

    private final JPanel empPanel;
    private JTable employeeTable;
    private JTextField searchField;
    private JComboBox<String> searchFilter;
    private DefaultTableModel tableModel;

    private JPanel detailsPanel;
    private JTextField idField, nameField, deptField, joinDateField, mobileField, emailField;
    private JButton backButton;
    private JScrollPane tableScrollPane;

    public ViewEmployee() {
        empPanel = new JPanel(null);
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        TitledBorder border = BorderFactory.createTitledBorder("View Employee");
        border.setTitleFont(titleFont);
        empPanel.setBorder(border);

        // Search components
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(20, 30, 60, 25);
        empPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(90, 30, 150, 25);
        empPanel.add(searchField);

        String[] filters = {"Employee ID", "Name", "Department"};
        searchFilter = new JComboBox<>(filters);
        searchFilter.setBounds(250, 30, 120, 25);
        empPanel.add(searchFilter);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(380, 30, 80, 25);
        searchButton.addActionListener(e -> searchEmployees());
        empPanel.add(searchButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(470, 30, 80, 25);
        refreshButton.addActionListener(e -> loadEmployeeDetails());
        empPanel.add(refreshButton);

        // Employee Table
        String[] columnNames = {"ID", "Name", "Department", "Joining Date", "Mobile Number", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        employeeTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        employeeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        employeeTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        employeeTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        
        tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBounds(20, 70, 700, 250);
        empPanel.add(tableScrollPane);

        SwingUtilities.invokeLater(this::loadEmployeeDetails);

        // Double-click to view details
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = employeeTable.getSelectedRow();
                    if (selectedRow != -1) {
                        showEmployeeDetails(selectedRow);
                    }
                }
            }
        });

        // Setup details panel (hidden by default)
        setupDetailsPanel();

        // Action buttons
        JButton viewButton = new JButton("View Details");
        viewButton.setBounds(100, 340, 120, 30);
        viewButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                showEmployeeDetails(selectedRow);
            } else {
                JOptionPane.showMessageDialog(empPanel, "Please select an employee first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        empPanel.add(viewButton);

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setBounds(250, 340, 140, 30);
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow != -1) {
                deleteEmployee(selectedRow);
            } else {
                JOptionPane.showMessageDialog(empPanel, "Please select an employee first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        empPanel.add(deleteButton);
    }

    public JPanel getPanel() {
        return empPanel;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
    }

    private void loadEmployeeDetails() {
        String query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail FROM addemployee";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // Clear existing data

            while (rs.next()) {
                String fullName = formatName(
                    rs.getString("firstname"),
                    rs.getString("middlename"),
                    rs.getString("lastname")
                );
                
                tableModel.addRow(new Object[]{
                    rs.getString("empId"),
                    fullName,
                    rs.getString("Department"),
                    rs.getString("dateOfJoining"),
                    rs.getString("mob"),
                    rs.getString("mail")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(empPanel, 
                "Error loading employee data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String formatName(String first, String middle, String last) {
        StringBuilder name = new StringBuilder();
        if (first != null && !first.trim().isEmpty()) name.append(first.trim());
        if (middle != null && !middle.trim().isEmpty()) name.append(" ").append(middle.trim());
        if (last != null && !last.trim().isEmpty()) name.append(" ").append(last.trim());
        return name.toString().trim();
    }

    private void searchEmployees() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadEmployeeDetails();
            return;
        }

        String filter = (String) searchFilter.getSelectedItem();
        String query;
        
        switch (filter) {
            case "Employee ID":
                query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail " +
                        "FROM addemployee WHERE empId LIKE ?";
                break;
            case "Name":
                query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail " +
                        "FROM addemployee WHERE CONCAT(firstname, ' ', middlename, ' ', lastname) LIKE ?";
                break;
            case "Department":
                query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail " +
                        "FROM addemployee WHERE Department LIKE ?";
                break;
            default:
                query = "SELECT empId, firstname, middlename, lastname, Department, dateOfJoining, mob, mail " +
                        "FROM addemployee";
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (!filter.equals("All")) {
                stmt.setString(1, "%" + searchText + "%");
            }
            
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);

            while (rs.next()) {
                String fullName = formatName(
                    rs.getString("firstname"),
                    rs.getString("middlename"),
                    rs.getString("lastname")
                );
                
                tableModel.addRow(new Object[]{
                    rs.getString("empId"),
                    fullName,
                    rs.getString("Department"),
                    rs.getString("dateOfJoining"),
                    rs.getString("mob"),
                    rs.getString("mail")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(empPanel, 
                "Error searching employees: " + e.getMessage(), 
                "Search Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void setupDetailsPanel() {
        detailsPanel = new JPanel(null);
        detailsPanel.setBounds(20, 70, 700, 250);
        detailsPanel.setVisible(false);
        empPanel.add(detailsPanel);

        // ID Field
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setBounds(20, 20, 120, 25);
        detailsPanel.add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 20, 200, 25);
        idField.setEditable(false);
        detailsPanel.add(idField);

        // Name Field
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(20, 50, 120, 25);
        detailsPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 50, 400, 25);
        nameField.setEditable(false);
        detailsPanel.add(nameField);

        // Department Field
        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setBounds(20, 80, 120, 25);
        detailsPanel.add(deptLabel);

        deptField = new JTextField();
        deptField.setBounds(150, 80, 200, 25);
        deptField.setEditable(false);
        detailsPanel.add(deptField);

        // Joining Date Field
        JLabel joinDateLabel = new JLabel("Joining Date:");
        joinDateLabel.setBounds(20, 110, 120, 25);
        detailsPanel.add(joinDateLabel);

        joinDateField = new JTextField();
        joinDateField.setBounds(150, 110, 200, 25);
        joinDateField.setEditable(false);
        detailsPanel.add(joinDateField);

        // Mobile Field
        JLabel mobileLabel = new JLabel("Mobile Number:");
        mobileLabel.setBounds(20, 140, 120, 25);
        detailsPanel.add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setBounds(150, 140, 200, 25);
        mobileField.setEditable(false);
        detailsPanel.add(mobileField);

        // Email Field
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setBounds(20, 170, 120, 25);
        detailsPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 170, 400, 25);
        emailField.setEditable(false);
        detailsPanel.add(emailField);

        // Back Button
        backButton = new JButton("Back to List");
        backButton.setBounds(300, 210, 150, 30);
        backButton.addActionListener(e -> switchToTableView());
        detailsPanel.add(backButton);
    }

    private void showEmployeeDetails(int selectedRow) {
        if (selectedRow < 0 || selectedRow >= tableModel.getRowCount()) {
            JOptionPane.showMessageDialog(empPanel, 
                "Invalid employee selection.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get data from the selected row
        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        deptField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        joinDateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        mobileField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        emailField.setText(tableModel.getValueAt(selectedRow, 5).toString());

        // Switch views
        tableScrollPane.setVisible(false);
        detailsPanel.setVisible(true);

        empPanel.revalidate();
        empPanel.repaint();
    }

    private void switchToTableView() {
        detailsPanel.setVisible(false);
        tableScrollPane.setVisible(true);

        empPanel.revalidate();
        empPanel.repaint();
    }

    private void deleteEmployee(int selectedRow) {
        String empId = tableModel.getValueAt(selectedRow, 0).toString();
        String empName = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(
            empPanel, 
            "Are you sure you want to delete employee:\n" +
            "ID: " + empId + "\n" +
            "Name: " + empName + "?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String query = "DELETE FROM addemployee WHERE empId = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, empId);
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(empPanel, 
                    "Employee deleted successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(empPanel, 
                "Error deleting employee: " + e.getMessage(), 
                "Deletion Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}