package vartak;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class addEvent {

    private final JPanel addEventPanel;
    private final JTextField eventNameField;
    private final JTextArea eventDescArea;
    private final JDateChooser eventDateChooser;
    private final JComboBox<String> employeeDropdown;
    private final JButton addButton, clearButton;

    public addEvent() {
        addEventPanel = new JPanel(null);
        addEventPanel.setBorder(BorderFactory.createTitledBorder("Add New Event"));
        addEventPanel.setBackground(new Color(255, 255, 204));

        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setBounds(30, 40, 100, 25);
        addEventPanel.add(nameLabel);

        eventNameField = new JTextField();
        eventNameField.setBounds(150, 40, 200, 25);
        addEventPanel.add(eventNameField);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(30, 80, 100, 25);
        addEventPanel.add(descLabel);

        eventDescArea = new JTextArea();
        JScrollPane descScroll = new JScrollPane(eventDescArea);
        descScroll.setBounds(150, 80, 300, 100);
        addEventPanel.add(descScroll);

        JLabel dateLabel = new JLabel("Event Date:");
        dateLabel.setBounds(30, 200, 100, 25);
        addEventPanel.add(dateLabel);

        eventDateChooser = new JDateChooser();
        eventDateChooser.setBounds(150, 200, 200, 25);
        addEventPanel.add(eventDateChooser);

        JLabel addedByLabel = new JLabel("Added By:");
        addedByLabel.setBounds(30, 240, 100, 25);
        addEventPanel.add(addedByLabel);

        employeeDropdown = new JComboBox<>();
        employeeDropdown.setBounds(150, 240, 200, 25);
        populateEmployeeDropdown();
        addEventPanel.add(employeeDropdown);

        addButton = new JButton("Add Event");
        addButton.setBounds(150, 290, 120, 30);
        addEventPanel.add(addButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(280, 290, 80, 30);
        addEventPanel.add(clearButton);

        addListeners();
    }

    private void populateEmployeeDropdown() {
        employeeDropdown.addItem("-- Select Employee --"); // Add this as the first option
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT firstname, middlename, lastname FROM addEmployee")) {
            
            while (rs.next()) {
                String firstName = rs.getString("firstname");
                String middleName = rs.getString("middlename");
                String lastName = rs.getString("lastname");
    
                // Handle potential null or empty middle names
                String fullName = firstName + " " +
                                  (middleName != null && !middleName.trim().isEmpty() ? middleName + " " : "") +
                                  lastName;
    
                employeeDropdown.addItem(fullName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading employee list: " + e.getMessage());
        }
    }
    
    private void addListeners() {
        clearButton.addActionListener(e -> {
            eventNameField.setText("");
            eventDescArea.setText("");
            eventDateChooser.setDate(null);
            employeeDropdown.setSelectedIndex(0);
        });

        addButton.addActionListener(e -> {
            String name = eventNameField.getText().trim();
            String description = eventDescArea.getText().trim();
            java.util.Date date = eventDateChooser.getDate();
            String addedBy = (String) employeeDropdown.getSelectedItem();

            if (name.isEmpty() || description.isEmpty() || date == null || addedBy == null) {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
                return;
            }

            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO events (eventName, eventDescription, eventDate, eventAddedBy) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setDate(3, sqlDate);
                stmt.setString(4, addedBy);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Event added successfully.");
                    clearButton.doClick();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add event.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        });
    }

    public JPanel getPanel() {
        return addEventPanel;
    }
}
