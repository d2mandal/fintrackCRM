package vartak;

import java.awt.Color;
import java.awt.Image;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class EmployeeAttendance {

    private final JTextField empIdField, nameField;
    private final JSpinner checkInField, checkOutField;
    private final JDateChooser dateChooser;
    private final JComboBox<String> statusComboBox, reasonTypeComboBox;
    private final JButton searchButton, submitButton;
    private final JLabel imageLabel;
    private final JPanel attendancePanel;
    //private JProgressBar progressBar;
    private Connection conn;

    public EmployeeAttendance() {
        attendancePanel = new JPanel(null);
        attendancePanel.setBorder(BorderFactory.createTitledBorder("Attendance Details"));

        // progressBar = new JProgressBar();
        //progressBar.setIndeterminate(true);
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdLabel.setBounds(20, 20, 100, 25);
        attendancePanel.add(empIdLabel);

        empIdField = new JTextField();
        empIdField.setBounds(130, 20, 100, 25);
        attendancePanel.add(empIdField);

        searchButton = new JButton("Search");
        searchButton.setBounds(240, 20, 80, 25);
        attendancePanel.add(searchButton);

        JLabel nameLabel = new JLabel("Employee Name:");
        nameLabel.setBounds(20, 60, 100, 25);
        attendancePanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 60, 200, 25);
        nameField.setEditable(false);
        attendancePanel.add(nameField);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(350, 60, 50, 25);
        attendancePanel.add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(400, 60, 100, 25);
        dateChooser.setDate(new Date());
        dateChooser.setEnabled(false);
        attendancePanel.add(dateChooser);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(20, 100, 100, 25);
        attendancePanel.add(statusLabel);

        statusComboBox = new JComboBox<>(new String[]{"PRESENT", "ABSENT"});
        statusComboBox.setBounds(130, 100, 100, 25);
        statusComboBox.setEnabled(false);

        attendancePanel.add(statusComboBox);

        JLabel reasonLabel = new JLabel("Reason:");
        reasonLabel.setBounds(340, 100, 50, 25);
        attendancePanel.add(reasonLabel);

        reasonTypeComboBox = new JComboBox<>(new String[]{"Sick Leave", "Casual Leave", "Annual Leave", "Maternity Leave", "Paternity Leave", "Unknown"});
        reasonTypeComboBox.setBounds(400, 100, 150, 25);
        reasonTypeComboBox.setEnabled(false);
        attendancePanel.add(reasonTypeComboBox);

        JLabel checkInLabel = new JLabel("Check-in Time:");
        checkInLabel.setBounds(20, 140, 100, 25);
        attendancePanel.add(checkInLabel);

        checkInField = new JSpinner(new SpinnerDateModel());
        checkInField.setBounds(130, 140, 100, 25);
        checkInField.setEditor(new JSpinner.DateEditor(checkInField, "HH:mm"));
        checkInField.setEnabled(false);
        attendancePanel.add(checkInField);

        JLabel checkOutLabel = new JLabel("Check-out Time:");
        checkOutLabel.setBounds(300, 140, 100, 25);
        attendancePanel.add(checkOutLabel);

        checkOutField = new JSpinner(new SpinnerDateModel());
        checkOutField.setBounds(400, 140, 100, 25);
        checkOutField.setEditor(new JSpinner.DateEditor(checkOutField, "HH:mm"));
        checkOutField.setEnabled(false);
        attendancePanel.add(checkOutField);

        imageLabel = new JLabel();
        imageLabel.setBounds(610, 20, 150, 160);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        attendancePanel.add(imageLabel);

        submitButton = new JButton("Submit");
        submitButton.setBounds(130, 190, 100, 30);
        attendancePanel.add(submitButton);

        searchButton.addActionListener(e -> fetchEmployeeDetails());

        statusComboBox.addActionListener(e -> updateFieldsBasedOnStatus());

        submitButton.addActionListener(e -> saveAttendance());
    }

    private void updateFieldsBasedOnStatus() {
        try {
            String checkstmt = "SELECT checkInTime, checkOutTime FROM empattendance WHERE empID = ? AND attendanceDate = ?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, empIdField.getText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            stmt.setString(2, dateFormat.format(dateChooser.getDate()));
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                if ("PRESENT".equals(statusComboBox.getSelectedItem())) {
                    checkInField.setEnabled(true);
                    checkOutField.setEnabled(false);
                    reasonTypeComboBox.setEnabled(false);
                }
            } //else if ("PRESENT".equals(statusComboBox.getSelectedItem()) && rs.getString("checkOutTime") == null)
            else if (rs.getString("checkOutTime") == null) {
                checkInField.setEnabled(false);
                checkOutField.setEnabled(true);
                reasonTypeComboBox.setEnabled(false);
            } else if ("ABSENT".equals(statusComboBox.getSelectedItem())) {
                checkInField.setEnabled(false);
                checkOutField.setEnabled(false);
                reasonTypeComboBox.setEnabled(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveAttendance() {
        String empId = empIdField.getText();
        String empName = nameField.getText();
        String status = (String) statusComboBox.getSelectedItem();
        String reason = reasonTypeComboBox.isEnabled() ? (String) reasonTypeComboBox.getSelectedItem() : null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(dateChooser.getDate());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String checkInFormatted = checkInField.isEnabled() ? timeFormat.format(checkInField.getValue()) : null;
        String checkOutFormatted = checkOutField.isEnabled() ? timeFormat.format(checkOutField.getValue()) : null;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234")) {
            String checkQuery = "SELECT checkInTime, checkOutTime FROM empattendance WHERE empID = ? AND attendanceDate = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, empId);
            checkStmt.setString(2, formattedDate);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                if (rs.getString("checkOutTime") == null && "PRESENT".equals(status)) {
                    String updateQuery = "UPDATE empattendance SET checkOutTime = ? WHERE empID = ? AND attendanceDate = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, checkOutFormatted);
                    updateStmt.setString(2, empId);
                    updateStmt.setString(3, formattedDate);
                    updateStmt.executeUpdate();
                    System.out.println("Check-out time updated!");
                } else {
                    System.out.println("Attendance already recorded for today.");
                }
            } else {
                String insertQuery = "INSERT INTO empattendance (empID, empName, attendanceDate, status, reason, checkInTime) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, empId);
                insertStmt.setString(2, empName);
                insertStmt.setString(3, formattedDate);
                insertStmt.setString(4, status);
                insertStmt.setString(5, reason);
                insertStmt.setString(6, checkInFormatted);
                insertStmt.executeUpdate();
                System.out.println("Attendance recorded successfully!");
            }

            refreshPanel();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void refreshPanel() {
        attendancePanel.removeAll();

        attendancePanel.revalidate();
        attendancePanel.repaint();
        new EmployeeAttendance(); // Create a new instance to reset fields
    }

    private void fetchEmployeeDetails() {
        // Implement employee search logic here
        try {
            String query = "SELECT firstname, middlename, lastname, image_path FROM addemployee WHERE empId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, empIdField.getText());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullname = rs.getString("firstname") + " " + rs.getString("middlename") + " " + rs.getString("lastname");
                nameField.setText(fullname);
                String imgpath = rs.getString("image_path");
                if (imgpath != null) {
                    ImageIcon icon = new ImageIcon(imgpath);
                    imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 160, Image.SCALE_SMOOTH)));
                }
            }
            statusComboBox.setEnabled(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public JPanel getPanel() {

        return attendancePanel;
    }
}
