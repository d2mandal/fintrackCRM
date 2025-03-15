package vartak;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

class AddNewEmployee extends JPanel {
    private Connection conn;
    private JTextField firstNameText, middleNameText, lastNameText, addressText,  pincodeText, mobText, emailText,ageText;
    private JDateChooser dateChooser;
    private JLabel  imageLabel,stateSelected;
    private JRadioButton male, female, other;
    private JButton submitBtn, backBtn, uploadButton;
    private String imagePath = "";
    private JComboBox<String> stateCombo;

    public AddNewEmployee(JPanel mainContentPanel, CardLayout cardLayout) {
        setLayout(null);
        setBackground(new Color(255, 255, 204));
       SwingUtilities.invokeLater(() -> {
           initComponents();
           addComponents();
       });
        
    }

   

    private void initComponents() {
        firstNameText = createTextField();
        middleNameText = createTextField();
        lastNameText = createTextField();
        ageText=createTextField();
        addressText = createTextField();
        //stateText = createTextField();
        pincodeText = createTextField();
        mobText = createTextField();
        emailText = createTextField();
        dateChooser = new JDateChooser();
        //ageResult = new JLabel();
        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        other = new JRadioButton("Other");
        ButtonGroup grp = new ButtonGroup();
        grp.add(male);
        grp.add(female);
        grp.add(other);

        submitBtn = new JButton("Submit");
        backBtn = new JButton("Back");
        uploadButton = new JButton("Upload Image");

        String[] states = {"","Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
        stateCombo = new JComboBox<>(states);
        
      

    dateChooser.getDateEditor().addPropertyChangeListener(e -> {
    if ("date".equals(e.getPropertyName())) {
        java.util.Date date = dateChooser.getDate();
        if (date == null) {  // Check for null before using date.getTime()
            ageText.setText(""); // Clear age field if no date is selected
            return;
        }
        long diff = new java.util.Date().getTime() - date.getTime();
        long diffYears = diff / (365L * 24 * 60 * 60 * 1000);
        ageText.setText(String.valueOf(diffYears));
        // ageText.setText(String.valueOf(diffYears));
    }
});

   
   
    submitBtn.addActionListener(e -> insertEmployeeData());
    uploadButton.addActionListener(e -> uploadImage());
}

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        return textField;
    }

   

    private void addComponents() {
        addLabel("First Name*:", 20, 20, 100, 25);
        addTextField(firstNameText, 140, 20, 200, 25);

        addLabel("Middle Name:", 20, 60, 100, 25);
        addTextField(middleNameText, 140, 60, 200, 25);

        addLabel("Last Name*:", 20, 100, 100, 25);
        addTextField(lastNameText, 140, 100, 200, 25);

        addLabel("Date Of Birth*:", 20, 140, 100, 25);
        dateChooser.setBounds(140, 140, 150, 25);
        add(dateChooser);

        addLabel("Age:", 20, 180, 100, 25);
        addTextField(ageText, 140, 180, 100, 25);

        addLabel("Gender*:", 20, 220, 100, 25);
        addRadioButtons(140, 220);

        addLabel("Address*:", 20, 260, 100, 25);
        addTextField(addressText, 140, 260, 200, 25);

        addLabel("State*:", 20, 300, 100, 25);
        stateCombo.setBounds(140, 300, 200, 25);
        add(stateCombo);

        stateSelected = new JLabel();
        stateSelected.setBounds(340, 300, 200, 25);
        add(stateSelected);
        stateCombo.addActionListener(e -> stateSelected.setText(stateCombo.getSelectedItem().toString()));
        

        addLabel("Pincode*:", 20, 340, 100, 25);
        addTextField(pincodeText, 140, 340, 150, 25);

        pincodeText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!pincodeText.getText().matches("\\d{6}")) {
                    JOptionPane.showMessageDialog(null, "Invalid Pincode! It must be exactly 6 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    pincodeText.requestFocus(); // Bring focus back to pincode field
                }
            }
        });
        

        addLabel("Mobile Number*:", 20, 380, 100, 25);
        addTextField(mobText, 140, 380, 200, 25);

        addLabel("Email*:", 20, 420, 100, 25);
        addTextField(emailText, 140, 420, 200, 25);

        submitBtn.setBounds(140, 500, 100, 30);
        add(submitBtn);

        backBtn.setBounds(20, 500, 100, 30);
        add(backBtn);

        imageLabel.setBounds(400, 20, 160, 150);
        add(imageLabel);

        uploadButton.setBounds(400, 180, 150, 30);
        add(uploadButton);
    }

    private void addLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.RED);
        label.setBounds(x, y, width, height);
        add(label);
    }

    private void addTextField(JTextField field, int x, int y, int width, int height) {
        field.setBounds(x, y, width, height);
        add(field);
    }

    private void addRadioButtons(int x, int y) {
        male.setBounds(x, y, 60, 25);
        female.setBounds(x + 70, y, 80, 25);
        other.setBounds(x + 160, y, 80, 25);
        add(male);
        add(female);
        add(other);
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath()).getImage().getScaledInstance(150, 160, Image.SCALE_SMOOTH));
            imageLabel.setText("");
            imageLabel.setIcon(imageIcon);
            imagePath = selectedFile.getAbsolutePath();
        }
    }

    //private boolean isSubmittedSuccessfully = false;
    private void insertEmployeeData() {
        if (!validateFields()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dateChooser.getDate() == null) {  // Check null before processing
            JOptionPane.showMessageDialog(this, "Please select a date of birth!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            ageText.setText(""); // Clear the age field if no date is selected
            return;
        }
    
       
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO addEmployee (firstname, middlename, lastname, dob, age, gender, address, state, pincode,  mob, mail,image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?,?,?)")) {
    
            pstmt.setString(1, firstNameText.getText());
            pstmt.setString(2, middleNameText.getText());
            pstmt.setString(3, lastNameText.getText());
            pstmt.setDate(4, new java.sql.Date(dateChooser.getDate().getTime()));
            // pstmt.setString(5, ageText.getText());
            pstmt.setInt(5, Integer.parseInt(ageText.getText().trim()));
            pstmt.setString(6, male.isSelected() ? "M" : (female.isSelected() ? "F" : "O"));
            pstmt.setString(7, addressText.getText());
            pstmt.setString(8, stateCombo.getSelectedItem().toString());
            pstmt.setString(9, pincodeText.getText());
            pstmt.setString(10, mobText.getText());
            pstmt.setString(11, emailText.getText());
            pstmt.setString(12, imagePath);
    
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                
                clearFields(); // Clear fields after successful entry
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Try again!", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void clearFields() {
        firstNameText.setText("");
        middleNameText.setText("");
        lastNameText.setText("");
        ageText.setText("");
        addressText.setText("");
        pincodeText.setText("");
        mobText.setText("");
        emailText.setText("");
      
        if (dateChooser != null) {
            dateChooser.setDate(null);
        }
        male.setSelected(false);
        female.setSelected(false);
        other.setSelected(false);
        stateCombo.setSelectedIndex(0); // Reset state combo box
        imageLabel.setText("No Image Selected"); // Reset image label
        imageLabel.setIcon(null); // Reset the image icon

       
    }
    

    private boolean validateFields() {
        return !firstNameText.getText().trim().isEmpty() &&
               !lastNameText.getText().trim().isEmpty() &&
               dateChooser.getDate() != null &&
               (male.isSelected() || female.isSelected() || other.isSelected()) &&
               !addressText.getText().trim().isEmpty() &&
               !mobText.getText().trim().isEmpty() &&
               !emailText.getText().trim().isEmpty();
    }

     public JPanel getPanel() {
         return this;
     }
}


