package vartak;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;

public class AddNewClient {

    private final JPanel newClientPanel;
    private JTextField clientNameText, addressText, contactText, emailText, projectNameText;
    private JDateChooser deadlineDateChooser;
    private JButton submitBtn, backBtn;
    private JDateChooser  dateChooserJoining;
    private JTextArea projectDesc;
    private JComboBox<String> projectManagerDropdown;
    
    public AddNewClient() {
        newClientPanel = new JPanel(null);
        newClientPanel.setBorder(BorderFactory.createTitledBorder("Add New Client"));
        newClientPanel.setBackground(new Color(255, 255, 204));
    
        initComponents();
        addComponents();
        loadEmployees();
    }

    private void initComponents() {
        clientNameText = createTextField();
        addressText = createTextField();
        contactText = createTextField();
        emailText = createTextField();
        projectNameText = createTextField();
        
        deadlineDateChooser = new JDateChooser();
        deadlineDateChooser.setDateFormatString("yyyy-MM-dd");

        dateChooserJoining = new JDateChooser();
        dateChooserJoining.setDateFormatString("yyyy-MM-dd");
        dateChooserJoining.setDate(new java.util.Date());

        projectDesc = new JTextArea();
        projectDesc.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        projectDesc.setLineWrap(true);
        projectDesc.setWrapStyleWord(true);
        
        projectManagerDropdown = new JComboBox<>();
        
        submitBtn = new JButton("Submit");
        backBtn = new JButton("Back");
        
        submitBtn.addActionListener(e -> insertClientData());
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        return textField;
    }

    private void addComponents() {
        addLabel("Client Name*:", 20, 20, 100, 25);
        addTextField(clientNameText, 120, 20, 350, 25);

        addLabel("Date of Added*:", 490, 20, 100, 25);
       addDateChooser(dateChooserJoining,590,20,100,25);

        addLabel("Email*:", 20, 60, 50, 25);
        addTextField(emailText, 70, 60, 200, 25);

        addLabel("Contact No*:", 300, 60, 100, 25);
        addTextField(contactText, 380, 60, 200, 25);

        addLabel("Project Name*:", 20, 100, 200, 25);
        addTextField(projectNameText, 120, 100, 250, 25);

        addLabel("Project Manager", 390, 100, 200, 25);
        addDropdown(projectManagerDropdown, 500, 100, 200, 25);
        
        addLabel("Deadline Date*:", 20, 140, 100, 25);
        addDateChooser(deadlineDateChooser,120, 140, 130, 25);

        addLabel("Project Description: ", 20, 180, 130, 25);
        addTextArea(projectDesc, 140, 180, 400, 80);
        
        submitBtn.setBounds(140, 280, 100, 30);
        newClientPanel.add(submitBtn);

        backBtn.setBounds(20, 280, 100, 30);
        newClientPanel.add(backBtn);
    }

    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.RED);
        label.setBounds(x, y, width, height);
        newClientPanel.add(label);
    }

    private void addTextField(JTextField field, int x, int y, int width, int height) {
        field.setBounds(x, y, width, height);
        newClientPanel.add(field);
    }
    private void addDateChooser(JDateChooser chooser, int x, int y, int width, int height) {
        chooser.setBounds(x, y, width, height);
        newClientPanel.add(chooser);
    }
 
    private void addTextArea(JTextArea area, int x, int y, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBounds(x, y, width, height);
        newClientPanel.add(scrollPane);
    }
    
    private void addDropdown(JComboBox<String> dropdown, int x, int y, int width, int height) {
        dropdown.setBounds(x, y, width, height);
        newClientPanel.add(dropdown);
    }

    private void loadEmployees() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT firstname,middlename,lastname FROM addemployee")) {
            while (rs.next()) {
                String fullName = rs.getString("firstname") + " " + rs.getString("middlename") + " " + rs.getString("lastname");
                projectManagerDropdown.addItem(fullName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void insertClientData() {
        if (!validateFields()) {
            JOptionPane.showMessageDialog(null, "Please fill in all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO clients (ClientName, mail, mob, projectName,projectManager, dateAdded, deadLine, projectDesc) VALUES (?, ?, ?, ?, ?, ?, ?,?)") ) {
            
            pstmt.setString(1, clientNameText.getText());
            pstmt.setString(2, emailText.getText());
            pstmt.setString(3, contactText.getText());
            pstmt.setString(4, projectNameText.getText());
            pstmt.setString(5, projectManagerDropdown.getSelectedItem().toString());
            pstmt.setDate(6, new java.sql.Date(System.currentTimeMillis())); // Auto-fetch current date
            pstmt.setDate(7, new java.sql.Date(deadlineDateChooser.getDate().getTime()));
            pstmt.setString(8,projectDesc.getText());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Client added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add client. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        clientNameText.setText("");
        //addressText.setText("");
        contactText.setText("");
        emailText.setText("");
        projectNameText.setText("");
        projectDesc.setText("");
        deadlineDateChooser.setDate(null);
    }

    private boolean validateFields() {
        return !clientNameText.getText().trim().isEmpty()
                //&& !addressText.getText().trim().isEmpty()
                && !contactText.getText().trim().isEmpty()
                && !emailText.getText().trim().isEmpty()
                && !projectNameText.getText().trim().isEmpty()
                && deadlineDateChooser.getDate() != null;
    }
    
    public JPanel getPanel() {
        return newClientPanel;
    }
}
