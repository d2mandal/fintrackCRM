
package vartak;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;

public class deleteClient {

    private final JPanel deleteClientPanel;
    private JLabel oldProjectDescLabel;
    private JComboBox<String> newProjectManagerDropdown;
    private JButton deleteBtn, clearBtn;
    private String oldProjectName;
    private JComboBox<String> clientDropdown;
    private JButton searchBtn;

    // Labels for old values
    private JLabel oldClientNameLabel, oldEmailLabel, oldContactLabel, oldProjectNameLabel;
    private JLabel oldProjectManagerLabel, oldDateAddedLabel, oldDeadlineLabel;
    private JLabel oldDescLblTitle;

    public deleteClient() {
        deleteClientPanel = new JPanel(null);
        deleteClientPanel.setBorder(BorderFactory.createTitledBorder("Delete Client"));
        deleteClientPanel.setBackground(new Color(255, 255, 204));

        deleteBtn = new JButton("Delete");
        clearBtn = new JButton("Clear");
        newProjectManagerDropdown = new JComboBox<>();

        initComponents();
        loadClientNames();
        addComponents();
        loadEmployees();

        clearBtn.addActionListener(e -> clearAllFields());

        deleteBtn.addActionListener(e -> {
            String selectedName = (String) clientDropdown.getSelectedItem();
            if (selectedName == null || selectedName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select a client to delete.");
                return;
            }
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this client?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteClientFromDatabase(selectedName);
            }
        });
    }

    private void initComponents() {
        clientDropdown = new JComboBox<>();
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> fetchClientDetails());

        oldClientNameLabel = new JLabel();
        oldEmailLabel = new JLabel();
        oldContactLabel = new JLabel();
        oldProjectNameLabel = new JLabel();
        oldProjectManagerLabel = new JLabel();
        oldDateAddedLabel = new JLabel();
        oldDeadlineLabel = new JLabel();

        oldProjectDescLabel = new JLabel();
        oldProjectDescLabel.setVerticalAlignment(JLabel.TOP);
        oldProjectDescLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        oldDescLblTitle = new JLabel("Description:");
    }

    private void addComponents() {
        deleteClientPanel.setLayout(null);

        JLabel selectClientLabel = new JLabel("Select Client:");
        selectClientLabel.setBounds(20, 20, 100, 25);
        deleteClientPanel.add(selectClientLabel);

        clientDropdown.setBounds(100, 20, 200, 25);
        deleteClientPanel.add(clientDropdown);

        searchBtn.setBounds(310, 20, 80, 25);
        deleteClientPanel.add(searchBtn);

        int labelX = 20, labelY = 60;
        int fieldXOld = 120;

        addLabelPair("Client Name:", oldClientNameLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Email:", oldEmailLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Contact:", oldContactLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Project Name:", oldProjectNameLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Project Manager:", oldProjectManagerLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Date Added:", oldDateAddedLabel, labelX, labelY, fieldXOld); labelY += 40;
        addLabelPair("Deadline:", oldDeadlineLabel, labelX, labelY, fieldXOld); labelY += 40;

        oldDescLblTitle.setBounds(labelX, labelY, 100, 25);
        deleteClientPanel.add(oldDescLblTitle);

        oldProjectDescLabel.setBounds(fieldXOld, labelY, 200, 80);
        deleteClientPanel.add(oldProjectDescLabel);

        clearBtn.setBounds(20, 450, 100, 30);
        deleteBtn.setBounds(130, 450, 100, 30);

        deleteClientPanel.add(deleteBtn);
        deleteClientPanel.add(clearBtn);
    }

    private void addLabelPair(String labelText, JLabel label, int labelX, int labelY, int fieldXOld) {
        JLabel title = new JLabel(labelText);
        title.setBounds(labelX, labelY, 150, 25);
        deleteClientPanel.add(title);

        label.setBounds(fieldXOld, labelY, 200, 25);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        deleteClientPanel.add(label);
    }

    private void fetchClientDetails() {
        String selectedName = (String) clientDropdown.getSelectedItem();
        if (selectedName == null || selectedName.isEmpty()) {
            clearOldDetails();
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clients WHERE ClientName = ?")) {
            stmt.setString(1, selectedName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    oldClientNameLabel.setText(rs.getString("ClientName"));
                    oldEmailLabel.setText(rs.getString("mail"));
                    oldContactLabel.setText(rs.getString("mob"));
                    oldProjectNameLabel.setText(rs.getString("projectName"));
                    oldProjectManagerLabel.setText(rs.getString("projectManager"));
                    oldDateAddedLabel.setText(rs.getString("dateAdded"));
                    oldDeadlineLabel.setText(rs.getString("deadline"));
                    
                    String description = rs.getString("projectDesc");
                        oldProjectDescLabel.setText("<html><body style='width: 200px'>" + description + "</body></html>");

                } else {
                    JOptionPane.showMessageDialog(null, "Client not found.");
                    clearOldDetails();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            clearOldDetails();
        }
    }

    private void clearOldDetails() {
        oldClientNameLabel.setText("");
        oldEmailLabel.setText("");
        oldContactLabel.setText("");
        oldProjectNameLabel.setText("");
        oldProjectManagerLabel.setText("");
        oldDateAddedLabel.setText("");
        oldDeadlineLabel.setText("");
        oldProjectDescLabel.setText("");
        oldProjectName = null;
    }

    private void clearAllFields() {
        clientDropdown.setSelectedIndex(-1); // No selection
        clearOldDetails();
    }

    private void deleteClientFromDatabase(String clientName) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM clients WHERE ClientName = ?")) {
            stmt.setString(1, clientName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Client deleted successfully.");
                loadClientNames();
                clearAllFields();
            } else {
                JOptionPane.showMessageDialog(null, "No client found to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting client: " + e.getMessage());
        }
    }

    private void loadClientNames() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ClientName FROM clients")) {
            clientDropdown.removeAllItems();
            while (rs.next()) {
                clientDropdown.addItem(rs.getString("ClientName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading client names: " + e.getMessage());
        }
    }

    private void loadEmployees() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT firstname, middlename, lastname FROM addemployee")) {
            newProjectManagerDropdown.removeAllItems();
            newProjectManagerDropdown.addItem("");
            while (rs.next()) {
                String fullName = rs.getString("firstname") + " ";
                if (rs.getString("middlename") != null && !rs.getString("middlename").isEmpty()) {
                    fullName += rs.getString("middlename") + " ";
                }
                fullName += rs.getString("lastname");
                newProjectManagerDropdown.addItem(fullName.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading employees: " + e.getMessage());
        }
    }

    public JPanel getPanel() {
        return deleteClientPanel;
    }
}
