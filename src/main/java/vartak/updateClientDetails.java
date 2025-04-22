
package vartak;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class updateClientDetails {

    private final JPanel updateClientPanel;
    private JTextField newClientNameText, newEmailText, newContactText, newProjectNameText;
    private JDateChooser newDeadlineDateChooser, newDateChooserJoining;
    private JTextArea newProjectDesc;
    private JComboBox<String> newProjectManagerDropdown;
    private JButton updateBtn, clearBtn;
    private String oldProjectName;
    private JComboBox<String> clientDropdown;
    private JButton searchBtn;
    private JTextArea oldProjectDescTextArea; // Changed to JTextArea

    // Labels for old values (keeping for other fields)
    private JLabel oldClientNameLabel, oldEmailLabel, oldContactLabel, oldProjectNameLabel;
    private JLabel oldProjectManagerLabel, oldDateAddedLabel, oldDeadlineLabel;
    private JLabel oldDescLblTitle; // To hold "Description:" label

    public updateClientDetails() {
        updateClientPanel = new JPanel(null);
        updateClientPanel.setBorder(BorderFactory.createTitledBorder("Update Client Details"));
        updateClientPanel.setBackground(new Color(255, 255, 204));

        newClientNameText = createTextField(false); // Initially non-editable
        newEmailText = createTextField(false);
        newContactText = createTextField(false);
        newProjectNameText = createTextField(false);
        newDeadlineDateChooser = new JDateChooser();
        newDeadlineDateChooser.setEnabled(false);
        newDateChooserJoining = new JDateChooser();
        newDateChooserJoining.setEnabled(false);
        newProjectNameText.setEnabled(true);
        newProjectNameText.setEditable(false);
        newProjectDesc = new JTextArea();
        newProjectDesc.setEditable(false);
        newProjectManagerDropdown = new JComboBox<>();
        newProjectManagerDropdown.setEnabled(false);
        updateBtn = new JButton("Update");
        clearBtn = new JButton("Clear");

        updateBtn.addActionListener(this::updateClientData);
        clearBtn.addActionListener(e -> clearFields());

        initComponents();
        loadClientNames();
        addComponents();
        loadEmployees();
    }

    private void initComponents() {
        clientDropdown = new JComboBox<>();
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> fetchClientDetails());

        // Initialize labels for old details
        oldClientNameLabel = new JLabel();
        oldEmailLabel = new JLabel();
        oldContactLabel = new JLabel();
        oldProjectNameLabel = new JLabel();
        oldProjectManagerLabel = new JLabel();
        oldDateAddedLabel = new JLabel();
        oldDeadlineLabel = new JLabel();

        oldProjectDescTextArea = new JTextArea(); // Initialize the JTextArea
        oldProjectDescTextArea.setEditable(false); // Make it non-editable for display
        oldProjectDescTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        oldDescLblTitle = new JLabel("Description:"); // Initialize the label for the old description
    }

    private JTextField createTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        tf.setEditable(editable);
        return tf;
    }

    private void addComponents() {
        updateClientPanel.setLayout(null);

        // Top selection
        JLabel selectClientLabel = new JLabel("Select Client:");
        selectClientLabel.setBounds(20, 20, 100, 25);
        updateClientPanel.add(selectClientLabel);

        clientDropdown.setBounds(100, 20, 200, 25);
        updateClientPanel.add(clientDropdown);

        searchBtn.setBounds(310, 20, 80, 25);
        updateClientPanel.add(searchBtn);

        int labelX = 20, labelY = 60;
        int fieldXOld = 120;
        int labelXNew = 350;
        int fieldXNew = 480;

        addEditablePair("Client Name:", oldClientNameLabel, newClientNameText, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditablePair("Email:", oldEmailLabel, newEmailText, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditablePair("Contact:", oldContactLabel, newContactText, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditablePair("Project Name:", oldProjectNameLabel, newProjectNameText, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditablePair("Project Manager:", oldProjectManagerLabel, newProjectManagerDropdown, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditableDateChooserPair("Date Added:", oldDateAddedLabel, newDateChooserJoining, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;
        addEditableDateChooserPair("Deadline:", oldDeadlineLabel, newDeadlineDateChooser, labelX, labelY, fieldXOld, labelXNew, fieldXNew);
        labelY += 40;

        // Old Description using JTextArea in JScrollPane
        oldDescLblTitle.setBounds(labelX, labelY, 100, 25); // Set bounds for the "Description:" label
        updateClientPanel.add(oldDescLblTitle);

        JScrollPane oldDescScrollPane = new JScrollPane(oldProjectDescTextArea);
        oldDescScrollPane.setBounds(fieldXOld, labelY, 200, 100); // Increased height to 100
        updateClientPanel.add(oldDescScrollPane);

        JLabel newDescLbl = new JLabel("New Description:");
        newDescLbl.setBounds(labelXNew, labelY, 150, 25);
        updateClientPanel.add(newDescLbl);
        JScrollPane scrollPane = new JScrollPane(newProjectDesc);
        scrollPane.setBounds(fieldXNew, labelY, 250, 100);
        updateClientPanel.add(scrollPane);

        updateBtn.setBounds(fieldXNew - 120, labelY + 110, 100, 30); // Adjusted button position
        clearBtn.setBounds(fieldXNew + 20, labelY + 110, 100, 30);   // Adjusted button position
        updateClientPanel.add(updateBtn);
        updateClientPanel.add(clearBtn);
    }

    private void addEditablePair(String labelText, JLabel oldLbl, JComponent newField, int labelX, int labelY, int fieldXOld, int labelXNew, int fieldXNew) {
        JLabel oldLblTitle = new JLabel(labelText);
        oldLblTitle.setBounds(labelX, labelY, 150, 25);
        updateClientPanel.add(oldLblTitle);

        oldLbl.setBounds(fieldXOld, labelY, 200, 25);
        oldLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateClientPanel.add(oldLbl);

        JLabel newLblTitle = new JLabel("New " + labelText);
        newLblTitle.setBounds(labelXNew, labelY, 150, 25);
        updateClientPanel.add(newLblTitle);

        newField.setBounds(fieldXNew, labelY, 250, 25);
        updateClientPanel.add(newField);
    }

    private void addEditableDateChooserPair(String labelText, JLabel oldLbl, JDateChooser newChooser, int labelX, int labelY, int fieldXOld, int labelXNew, int fieldXNew) {
        JLabel oldLblTitle = new JLabel(labelText);
        oldLblTitle.setBounds(labelX, labelY, 150, 25);
        updateClientPanel.add(oldLblTitle);

        oldLbl.setBounds(fieldXOld, labelY, 200, 25);
        oldLbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateClientPanel.add(oldLbl);

        JLabel newLblTitle = new JLabel("New " + labelText);
        newLblTitle.setBounds(labelXNew, labelY, 150, 25);
        updateClientPanel.add(newLblTitle);

        newChooser.setBounds(fieldXNew, labelY, 250, 25);
        updateClientPanel.add(newChooser);
    }

    private void loadClientNames() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ClientName FROM clients")) {
            clientDropdown.removeAllItems(); // Clear existing items
            while (rs.next()) {
                clientDropdown.addItem(rs.getString("ClientName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading client names: " + e.getMessage());
        }
    }

    
    private void fetchClientDetails() {
        String selectedName = (String) clientDropdown.getSelectedItem();
        if (selectedName == null || selectedName.isEmpty()) {
            clearOldDetails();
            clearNewFields();
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
                    oldProjectDescTextArea.setText(rs.getString("projectDesc"));

                    oldProjectName = rs.getString("projectName");

                    // Populate new fields with old values for editing AND ENABLE THEM
                    newClientNameText.setText(rs.getString("ClientName"));
                    newClientNameText.setEditable(true);
                    newEmailText.setText(rs.getString("mail"));
                    newEmailText.setEditable(true);
                    newContactText.setText(rs.getString("mob"));
                    newContactText.setEditable(true);
                    newProjectNameText.setText(rs.getString("projectName"));
                    newProjectNameText.setEditable(true); // Enable Project Name for editing
                    newProjectManagerDropdown.setSelectedItem(rs.getString("projectManager"));
                    newProjectManagerDropdown.setEnabled(true);
                    newDateChooserJoining.setDate(rs.getDate("dateAdded"));
                    newDateChooserJoining.setEnabled(true); // Enable Date Added for editing
                    newDeadlineDateChooser.setDate(rs.getDate("deadline"));
                    newDeadlineDateChooser.setEnabled(true); // Enable Deadline for editing
                    newProjectDesc.setText(rs.getString("projectDesc"));
                    newProjectDesc.setEditable(true);   // Enable Description for editing

                } else {
                    JOptionPane.showMessageDialog(null, "Client not found.");
                    clearOldDetails();
                    clearNewFields();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
            clearOldDetails();
            clearNewFields();
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
        oldProjectDescTextArea.setText(""); // Clear the JTextArea
        oldProjectName = null;
    }

    private void clearNewFields() {
        newClientNameText.setText("");
        newClientNameText.setEditable(false);
        newEmailText.setText("");
        newEmailText.setEditable(false);
        newContactText.setText("");
        newContactText.setEditable(false);
        newProjectNameText.setText("");
        newProjectNameText.setEditable(false);
        newProjectManagerDropdown.setSelectedIndex(0);
        newProjectManagerDropdown.setEnabled(false);
        newDateChooserJoining.setDate(null);
        newDeadlineDateChooser.setDate(null);
        newProjectDesc.setText("");
        newProjectDesc.setEditable(false);
    }

    private void loadEmployees() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT firstname, middlename, lastname FROM addemployee")) {
            newProjectManagerDropdown.removeAllItems(); // Clear existing items
            newProjectManagerDropdown.addItem(""); // Add a default empty selection
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

    private void updateClientData(ActionEvent e) {
        String updatedClientName = newClientNameText.getText().trim();
        String updatedEmail = newEmailText.getText().trim();
        String updatedContact = newContactText.getText().trim();
        String updatedProjectName = newProjectNameText.getText().trim();
        Object selectedManager = newProjectManagerDropdown.getSelectedItem();
        String updatedProjectManager = (selectedManager == null || selectedManager.toString().isEmpty()) ? null : selectedManager.toString();
        Date updatedDateAdded = newDateChooserJoining.getDate();
        Date updatedDeadline = newDeadlineDateChooser.getDate();
        String updatedProjectDesc = newProjectDesc.getText().trim();

        if (oldProjectName == null || oldProjectName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please search for a client to update.");
            return;
        }

        if (updatedClientName.isEmpty() || updatedEmail.isEmpty() || updatedContact.isEmpty() || updatedProjectName.isEmpty() || updatedDateAdded == null || updatedDeadline == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all the required fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE clients SET ClientName=?, mail=?, mob=?, projectName=?, projectManager=?, dateAdded=?, deadline=?, projectDesc=? WHERE projectName=?")) {

            pstmt.setString(1, updatedClientName);
            pstmt.setString(2, updatedEmail);
            pstmt.setString(3, updatedContact);
            pstmt.setString(4, updatedProjectName);
            pstmt.setString(5, updatedProjectManager);
            pstmt.setDate(6, new java.sql.Date(updatedDateAdded.getTime()));
            pstmt.setDate(7, new java.sql.Date(updatedDeadline.getTime()));
            pstmt.setString(8, updatedProjectDesc);
            pstmt.setString(9, updatedProjectName); // where clause

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Client details updated successfully!");
                oldProjectName = updatedProjectName; // update for next edit
                loadClientNames(); // Refresh client dropdown
                fetchClientDetails(); // Refresh displayed details
            } else {
                JOptionPane.showMessageDialog(null, "Update failed. Client with project name '" + oldProjectName + "' not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        clearOldDetails();
        clearNewFields();
    }

    public JPanel getPanel() {
        return updateClientPanel;
    }
}