package vartak;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("FINTRACK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());

        // Create top panel for the clock and main options
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 150));

        // Clock panel
        JPanel clockPanel = new JPanel();
        clockPanel.setBackground(Color.DARK_GRAY);
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        JLabel clockLabel = new JLabel(getCurrentTime());
        clockLabel.setFont(new Font("Arial", Font.BOLD, 24));
        clockLabel.setForeground(Color.CYAN);
        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        clockPanel.add(clockLabel);
        clockPanel.add(dateLabel);

        // Refresh the clock every second
        Timer timer = new Timer(1000, _ -> clockLabel.setText(getCurrentTime()));
        timer.start();

        // Buttons panel for main options
        JPanel mainOptionsPanel = new JPanel(new GridLayout(2, 5));
        mainOptionsPanel.setBackground(Color.GRAY);
        String[] mainOptions = {
                "Update Existing Account", "Account Details", "Transactions",
                "Search Accounts", "Customers List", "Create New Account",
                "Deposit", "Transfer", "Withdrawal", "Check Balance"
        };

        // Main content panel with CardLayout
        JPanel mainContentPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);

        // Default "Home" Panel
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(Color.LIGHT_GRAY);
        JLabel homeLabel = new JLabel("FINTRACK");
        homeLabel.setFont(new Font("Arial", Font.BOLD, 25));
        homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeLabel.setVerticalAlignment(SwingConstants.CENTER);
        homePanel.add(homeLabel, BorderLayout.CENTER);

        // Add "Home" panel to CardLayout
        mainContentPanel.add(homePanel, "Home");

        // Add other panels dynamically
        for (String option : mainOptions) {
            JButton button = new JButton(option);
            mainOptionsPanel.add(button);

            // Add placeholder panels for options
            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.setBackground(Color.LIGHT_GRAY);
            JLabel optionLabel = new JLabel(option);
            optionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            optionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            optionPanel.add(optionLabel, BorderLayout.CENTER);

            // Add to CardLayout
            mainContentPanel.add(optionPanel, option);

            // ActionListener for buttons
            button.addActionListener((@SuppressWarnings("unused") var e) -> {
                switch (option) {
                    case "Create New Account" -> {
                        // Open CreateNewAccount panel
                        AddNewEmployee createNewAccount = new AddNewEmployee(mainContentPanel, cardLayout);
                        mainContentPanel.add(createNewAccount.getPanel(), "CreateNewAccount");
                        cardLayout.show(mainContentPanel, "CreateNewAccount");
                    }
                    case "Account Details" -> {
                        // Open AccountDetails panel
                        AccountDetails accountDetails = new AccountDetails(mainContentPanel, cardLayout);
                        mainContentPanel.add(accountDetails.getPanel(), "AccountDetails");
                        cardLayout.show(mainContentPanel, "AccountDetails");
                    }
                    case "Update Existing Account" ->{
                        String accountNumber=JOptionPane.showInputDialog(mainContentPanel,"Please enter the account number to update: ","Enter Account Number",JOptionPane.QUESTION_MESSAGE);
                        if(accountNumber==null || accountNumber.trim().isEmpty()){
                            accountNumber="1234";
                            JOptionPane.showMessageDialog(mainContentPanel,
                            "No account number entered. Using default account number:"+accountNumber,"Default Account",JOptionPane.INFORMATION_MESSAGE);

                        }
                        UpdateExistingAccount updateExistingAccount=new UpdateExistingAccount(mainContentPanel,cardLayout);
                       // updateExistingAccount.setAccountNumber(accountNumber);
                        mainContentPanel.add(updateExistingAccount.getPanel(),"UpdateExistingAccount");
                        cardLayout.show(mainContentPanel,"UpdateExistingAccount");
                    }
                    default -> // Switch to the selected option panel
                        cardLayout.show(mainContentPanel, option);
                }
            });
            
        }

        // Add clock and main options to top panel
        topPanel.add(clockPanel, BorderLayout.WEST);
        topPanel.add(mainOptionsPanel, BorderLayout.CENTER);

        // Side panel for utilities
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(5, 1));
        sidePanel.setBackground(Color.LIGHT_GRAY);
        sidePanel.setPreferredSize(new Dimension(160, frame.getHeight()));
        String[] sideOptions = {"User Settings", "Calculator", "Notepad", "Currency Converter", "Administration Panel"};
        for (String option : sideOptions) {
            JButton button = new JButton(option);
            sidePanel.add(button);
        }

        // Add panels to the frameṇ
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainContentPanel, BorderLayout.CENTER);




        // Set frame visibility
        frame.setVisible(true);
    }

    // Method to get current time
    private static String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a").format(new Date());
    }

    // Method to get current date
    private static String getCurrentDate() {
        return new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date());
    }
}

