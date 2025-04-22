package vartak;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class deleteEvent {

    private final JPanel deleteEventPanel;
    private final JComboBox<String> eventComboBox;
    private final JLabel eventDateLabel;
    private final JLabel eventNameLabel;
    private final JLabel eventDescLabel;

    public deleteEvent() {
        deleteEventPanel = new JPanel();
        deleteEventPanel.setLayout(null);
        deleteEventPanel.setBorder(BorderFactory.createTitledBorder("Delete Event"));
        deleteEventPanel.setBackground(new Color(255, 255, 204));

        JLabel selectEventLabel = new JLabel("Select Event:");
        selectEventLabel.setBounds(30, 30, 100, 25);
        deleteEventPanel.add(selectEventLabel);

        eventComboBox = new JComboBox<>();
        eventComboBox.setBounds(140, 30, 300, 25);
        deleteEventPanel.add(eventComboBox);

        eventDateLabel = new JLabel();
        eventDateLabel.setBounds(30, 70, 400, 25);
        deleteEventPanel.add(eventDateLabel);

        eventNameLabel = new JLabel();
        eventNameLabel.setBounds(30, 100, 400, 25);
        deleteEventPanel.add(eventNameLabel);

        eventDescLabel = new JLabel();
        eventDescLabel.setBounds(30, 130, 500, 25);
        deleteEventPanel.add(eventDescLabel);

        JButton deleteButton = new JButton("Delete Event");
        deleteButton.setBounds(140, 180, 150, 30);
        deleteEventPanel.add(deleteButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(300, 180, 100, 30);
        deleteEventPanel.add(clearButton);

        loadEvents();

        eventComboBox.addActionListener(e -> loadEventDetails((String) eventComboBox.getSelectedItem()));
        deleteButton.addActionListener(e -> deleteEvent());
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
                eventDateLabel.setText("<html><b>[" + rs.getDate("eventDate") + "]</b></html>");
                eventNameLabel.setText("<html><b>" + eventName + "</b></html>");
                eventDescLabel.setText(rs.getString("eventDescription"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading event details: " + e.getMessage());
        }
    }

    private void deleteEvent() {
        String selectedEvent = (String) eventComboBox.getSelectedItem();
        if (selectedEvent == null) return;

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the event '" + selectedEvent + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement ps = conn.prepareStatement("DELETE FROM events WHERE eventName = ?")) {
            ps.setString(1, selectedEvent);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Event deleted successfully.");
                eventComboBox.removeItem(selectedEvent);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Delete failed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting event: " + e.getMessage());
        }
    }

    private void clearFields() {
        eventComboBox.setSelectedIndex(-1);
        eventDateLabel.setText("");
        eventNameLabel.setText("");
        eventDescLabel.setText("");
    }

    public JPanel getPanel() {
        return deleteEventPanel;
    }
}