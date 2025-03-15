package vartak;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TableLayoutExample {
    public static void main(String[] args) {
        // Create frame
        JFrame frame = new JFrame("Form with Image Upload");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        //gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Left Side: Labels and TextFields (First Column)
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.anchor = GridBagConstraints.WEST;
        frame.add(new JLabel("First Name"), gbc);

        gbc.gridy++;
        frame.add(new JLabel("Middle Name"), gbc);

        gbc.gridy++;
        frame.add(new JLabel("Last Name"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField firstNameText = new JTextField(15);
        frame.add(firstNameText, gbc);

        gbc.gridy++;
        JTextField middleNameText = new JTextField(15);
        frame.add(middleNameText, gbc);

        gbc.gridy++;
        JTextField lastNameText = new JTextField(15);
        frame.add(lastNameText, gbc);

        // Right Side: Image Label (Second Column, First Row)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 3; // Span 3 rows
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        frame.add(imageLabel, gbc);

        // Upload Button (Second Row, Second Column)
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridheight = 1; // Reset row span
        gbc.anchor = GridBagConstraints.NORTH;
        JButton uploadButton = new JButton("Upload Image");
        frame.add(uploadButton, gbc);

        // Upload Button Action
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
                
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath())
                            .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    imageLabel.setText("");
                    imageLabel.setIcon(imageIcon);
                }
            }
        });

        // Show frame
        frame.setVisible(true);
    }
}
