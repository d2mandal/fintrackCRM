package vartak;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class updateEvent {

    private final JPanel updateEventPanel;
    private final JComboBox<String> eventComboBox;
    private final JTextField nameField;
    private final JTextArea descriptionArea;
    private final JSpinner dateSpinner;

    public updateEvent() {
        updateEventPanel = new JPanel();
        updateEventPanel.setLayout(null);
        updateEventPanel.setBorder(BorderFactory.createTitledBorder("Update Event"));
        updateEventPanel.setBackground(new Color(255, 255, 204));

        JLabel selectEventLabel = new JLabel("Select Event:");
        selectEventLabel.setBounds(30, 30, 100, 25);
        updateEventPanel.add(selectEventLabel);

        eventComboBox = new JComboBox<>();
        eventComboBox.setBounds(140, 30, 300, 25);
        updateEventPanel.add(eventComboBox);

        JLabel nameLabel = new JLabel("Event Name:");
        nameLabel.setBounds(30, 70, 100, 25);
        updateEventPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 70, 300, 25);
        updateEventPanel.add(nameField);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(30, 110, 100, 25);
        updateEventPanel.add(descLabel);

        descriptionArea = new JTextArea();
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBounds(140, 110, 300, 100);
        updateEventPanel.add(descScroll);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(30, 220, 100, 25);
        updateEventPanel.add(dateLabel);

        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        dateSpinner.setBounds(140, 220, 150, 25);
        updateEventPanel.add(dateSpinner);

        JButton updateButton = new JButton("Update Event");
        updateButton.setBounds(140, 270, 150, 30);
        updateEventPanel.add(updateButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(300, 270, 100, 30);
        updateEventPanel.add(clearButton);

        loadEvents();

        eventComboBox.addActionListener(e -> loadEventDetails((String) eventComboBox.getSelectedItem()));
        updateButton.addActionListener(e -> updateEventDetails());
        clearButton.addActionListener(e -> clearFields());
    }

    private void loadEvents() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT eventName FROM events ORDER BY eventDate ASC")) {
            while (rs.next()) {
                eventComboBox.addItem(rs.getString("eventName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading events: " + e.getMessage());
        }
    }

    private void loadEventDetails(String eventName) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement ps = conn.prepareStatement("SELECT eventDate, eventDescription FROM events WHERE eventName = ?")) {
            ps.setString(1, eventName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nameField.setText(eventName);
                descriptionArea.setText(rs.getString("eventDescription"));
                dateSpinner.setValue(rs.getDate("eventDate"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading event details: " + e.getMessage());
        }
    }

    private void updateEventDetails() {
        String newName = nameField.getText();
        String newDesc = descriptionArea.getText();
        java.util.Date newDate = (java.util.Date) dateSpinner.getValue();
        String selectedEvent = (String) eventComboBox.getSelectedItem();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement ps = conn.prepareStatement("UPDATE events SET eventName = ?, eventDescription = ?, eventDate = ? WHERE eventName = ?")) {
            ps.setString(1, newName);
            ps.setString(2, newDesc);
            ps.setDate(3, new java.sql.Date(newDate.getTime()));
            ps.setString(4, selectedEvent);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Event updated successfully.");
                eventComboBox.removeAllItems();
                //loadEvents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Update failed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating event: " + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        descriptionArea.setText("");
        dateSpinner.setValue(new java.util.Date());
    }

    public JPanel getPanel() {
        return updateEventPanel;
    }
}