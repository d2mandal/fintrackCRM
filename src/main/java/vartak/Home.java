package vartak;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Home extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel, homePanel, leftPanel, menuPanel, topPanel;
    private JButton homeMenu, empMenu, clientsMenu, leadsMenu, eventsMenu;
    private JButton option1, option2, option3, option4, option5, option6;
    private String currentMode = "";

    public Home(){
        initComponents();
        homePanel.setBackground(new Color(255, 255, 204));
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        homePanel = new JPanel();
        mainContentPanel.add(homePanel, "HomePanel");
        mainContentPanel.setBackground(new Color(255, 255, 204));

        leftPanel = createLeftPanel();
        menuPanel = createMenuPanel();

        topPanel = new JPanel(new BorderLayout());
        topPanel.add(menuPanel, BorderLayout.CENTER);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        leftPanel.setBounds(0, 0, 250, 700);
        topPanel.setBounds(250, 0, 800, 50);
        mainContentPanel.setBounds(250, 50, 800, 700);

        add(leftPanel);
        add(topPanel);
        add(mainContentPanel);

        setSize(1050, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setBackground(new Color(255, 102, 51));

        homeMenu = createMenuButton("Home", e -> showPanel("HomePanel"));
        empMenu = createMenuButton("Employee", e -> showOptionButtons("Employee"));
        clientsMenu = createMenuButton("Clients", e -> showOptionButtons("Clients"));
        leadsMenu = createMenuButton("Leads", e -> showOptionButtons("Leads"));
        eventsMenu = createMenuButton("Events", e -> {
        });

        panel.add(homeMenu);
        panel.add(empMenu);
        panel.add(clientsMenu);
        panel.add(leadsMenu);
        panel.add(eventsMenu);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBackground(new Color(255, 255, 204));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        option1 = createOptionButton("");
        option2 = createOptionButton("");
        option3 = createOptionButton("");
        option4 = createOptionButton("");
        option5 = createOptionButton("");
        option6 = createOptionButton("");

        option1.setBounds(3, 100, 244, 40);
        option2.setBounds(3, 140, 244, 40);
        option3.setBounds(3, 180, 244, 40);
        option4.setBounds(3, 220, 244, 40);
        option5.setBounds(3, 260, 244, 40);
        option6.setBounds(3, 300, 244, 40);

        panel.add(option1);
        panel.add(option2);
        panel.add(option3);
        panel.add(option4);
        panel.add(option5);
        panel.add(option6);

        hideOptionButtons();

        return panel;
    }

    private JButton createMenuButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 102, 51));
        button.setBorder(BorderFactory.createBevelBorder(0));
        button.addActionListener(action);
        return button;
    }

    private JButton createOptionButton(String text) {
        JButton button = new JButton(text);
        button.setVisible(false);
        return button;
    }

    private void showPanel(String panelName) {
        cardLayout.show(mainContentPanel, panelName);
        hideOptionButtons();
    }

    private void showOptionButtons(String mode) {
        currentMode = mode;
        switch (mode) {
            case "Employee" -> {
                option1.setText("Attendance");
                option2.setText("View Employees");
                option3.setText("Add New Employee");
                option4.setText("Update Employee");
                option5.setText("Delete Employee");
                option6.setText("Payroll");

                option1.addActionListener(e -> openEmployeeAttendance());
                option2.addActionListener(e -> System.out.println("Viewing Employees"));
                option3.addActionListener(e -> openAddNewEmployee());
                option4.addActionListener(e -> System.out.println("Updating Employee"));
                option5.addActionListener(e -> System.out.println("Deleting Employee"));
                option6.addActionListener(e -> System.out.println("Payroll"));
            }
            case "Clients" -> {
                option1.setText("View Project");
                option2.setText("Update Project Status");
                option3.setText("View Client");
                option4.setText("Update Client Details");
                option5.setText("Delete Client");
                option6.setVisible(false); // Not needed for Clients

                option1.addActionListener(e -> System.out.println("Viewing Projects"));
                option2.addActionListener(e -> System.out.println("Updating Project Status"));
                option3.addActionListener(e -> System.out.println("Viewing Clients"));
                option4.addActionListener(e -> System.out.println("Updating Client Details"));
                option5.addActionListener(e -> System.out.println("Deleting Client"));
            }
            case "Leads" -> {
                option1.setText("View All Leads");
                option2.setText("Add New Leads");
                option3.setText("Update Lead");
                option4.setText("Delete Leads");

                // Make only relevant buttons visible
                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                option4.setVisible(true);
                option5.setVisible(false); // Hide unused buttons
                option6.setVisible(false); // Hide unused buttons

                // Assign action listeners
                option1.addActionListener(e -> System.out.println("Viewing All Leads"));
                option2.addActionListener(e -> System.out.println("Adding New Lead"));
                option3.addActionListener(e -> System.out.println("Updating Lead"));
                option4.addActionListener(e -> System.out.println("Deleting Lead"));
            }
        }

        // Show only relevant buttons
        option1.setVisible(true);
        option2.setVisible(true);
        option3.setVisible(true);
        option4.setVisible(true);
        option5.setVisible(true);
        option6.setVisible(mode.equals("Employee"));
    }

    private void hideOptionButtons() {
        option1.setVisible(false);
        option2.setVisible(false);
        option3.setVisible(false);
        option4.setVisible(false);
        option5.setVisible(false);
        option6.setVisible(false);
    }

    private void openEmployeeAttendance() {
        EmployeeAttendance employeeAttendance = new EmployeeAttendance();
        mainContentPanel.add(employeeAttendance.getPanel(), "EmployeeAttendance");
        cardLayout.show(mainContentPanel, "EmployeeAttendance");
    }

    private void openAddNewEmployee() {
        AddNewEmployee addNewEmployee = new AddNewEmployee(mainContentPanel, cardLayout);
        mainContentPanel.add(addNewEmployee.getPanel(), "AddNewEmployee");
        cardLayout.show(mainContentPanel, "AddNewEmployee");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}

