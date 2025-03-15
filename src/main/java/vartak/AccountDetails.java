package vartak;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AccountDetails {
    private final JPanel panel;

    public AccountDetails(JPanel mainContentPanel, CardLayout cardLayout) {
        // Panel setup
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(220, 240, 255));

        // Title label
        JLabel titleLabel = new JLabel("Account Details:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(20, 20, 300, 30);
        panel.add(titleLabel);

        // Search Accounts section
        JLabel searchLabel = new JLabel("Search Accounts");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchLabel.setBounds(20, 70, 150, 25);
        panel.add(searchLabel);

        String[] searchLabels = {"Account Number:", "Account Title:", "NIC Number:"};
        int y = 110; // Starting Y position for search fields
        for (String label : searchLabels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(20, y, 150, 25);
            panel.add(lbl);

            JTextField textField = new JTextField();
            textField.setBounds(180, y, 200, 25);
            panel.add(textField);

            y += 40; // Move to the next row
        }

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(400, 180, 100, 25);
        panel.add(searchButton);

        // Profile Picture and Signature section
        JLabel profilePictureLabel = new JLabel("Profile Picture");
        profilePictureLabel.setBounds(20, 230, 150, 25);
        panel.add(profilePictureLabel);

        JLabel signatureLabel = new JLabel("Signature");
        signatureLabel.setBounds(220, 230, 150, 25);
        panel.add(signatureLabel);

        JLabel profilePicture = new JLabel(new ImageIcon("placeholder.png")); // Placeholder for the image
        profilePicture.setBounds(100, 200, 150, 100);
        panel.add(profilePicture);

        JLabel signature = new JLabel(new ImageIcon("placeholder.png")); // Placeholder for the image
        signature.setBounds(220, 260, 150, 100);
        panel.add(signature);

        // Details section
        String[] detailLabels = {
            "Customer Name:", "Account Number:", "Account Title:", "Account Type:",
            "Nationality:", "Date of Birth:", "Gender:", "Company Name:",
            "Occupation:", "Postal Address:", "Phone Number:", "NIC Number:",
            "Email Address:", "Current Balance:"
        };
        y = 380; // Starting Y position for details
        for (String label : detailLabels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(20, y, 150, 25);
            panel.add(lbl);

            JTextField textField = new JTextField();
            textField.setBounds(180, y, 200, 25);
            textField.setEditable(false); // Make it read-only
            panel.add(textField);

            y += 40; // Move to the next row
        }

        // Back button to navigate to Home
        JButton backButton = new JButton("Back");
        backButton.setBounds(20, y + 10, 100, 30);
        panel.add(backButton);

        // Back button action listener
        backButton.addActionListener(_ -> cardLayout.show(mainContentPanel, "Home"));
    }

    public JPanel getPanel() {
        return panel;
    }
}
