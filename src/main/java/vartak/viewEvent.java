
package vartak;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class viewEvent {

    private final JPanel viewEventPanel;

    public viewEvent() {
        viewEventPanel = new JPanel();
        viewEventPanel.setLayout(new BoxLayout(viewEventPanel, BoxLayout.Y_AXIS));
        viewEventPanel.setBorder(BorderFactory.createTitledBorder("View All Events"));
        viewEventPanel.setBackground(new Color(255, 255, 204));

        // Load and display events
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT eventDate, eventName, eventDescription FROM events ORDER BY eventDate ASC")) {

            while (rs.next()) {
                String date = rs.getDate("eventDate").toString();
                String name = rs.getString("eventName");
                String description = rs.getString("eventDescription");

                // Event panel for each event
                JPanel eventPanel = new JPanel();
                eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
                eventPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Event name and date in bold
                JLabel eventNameLabel = new JLabel("<html><b>" + name + "</b></html>");
                JLabel eventDateLabel = new JLabel("<html><b>[" + date + "]</b></html>");
                JLabel eventDescLabel = new JLabel(description);

                eventPanel.add(eventDateLabel);
                eventPanel.add(eventNameLabel);
                eventPanel.add(eventDescLabel);

                // Horizontal line
                JSeparator separator = new JSeparator();
                separator.setPreferredSize(new Dimension(500, 1)); // Adjust length as needed
                eventPanel.add(separator);

                // Add the event panel to the main panel
                viewEventPanel.add(eventPanel);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching events: " + e.getMessage());
        }
    }

    public JPanel getPanel() {
        return viewEventPanel;
    }
}
