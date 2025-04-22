package vartak;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Home extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContentPanel, homePanel, leftPanel, menuPanel, topPanel;
    private JButton homeMenu, empMenu, clientsMenu, leadsMenu, eventsMenu;
    private JButton option1, option2, option3, option4, option5, option6;
    private JLabel title;
    private String currentMode = "";
    private String userRole = ""; // Added userRole variable
    private JPanel loginPanel;
        private JTextField loginUsernameField;
        private JPasswordField loginPasswordField;
        private JRadioButton adminRadio, userRadio;
        private ButtonGroup roleGroup;
        private JLabel userInfoLabel; // To display user info
private JButton signoutButton; // Signout button
private static final String DB_URL = "jdbc:mysql://localhost:3306/fintrackdbs";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";
    
    public Home() {
        initComponents();
        homePanel.setBackground(new Color(255, 255, 204));
    }

    public JPanel getPanel() {
        return mainContentPanel;
    }
private void initComponents() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);

    // Initialize login first (without trying to hide main panels)
    initializeLogin();
    
    // Initialize main application components
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
    mainContentPanel.setBounds(250, 50, 1100, 900);

    // Add components but don't make them visible yet
    add(leftPanel);
    add(topPanel);
    add(mainContentPanel);
    leftPanel.setVisible(false);
    topPanel.setVisible(false);
    mainContentPanel.setVisible(false);

    setSize(1050, 600);
    setLocationRelativeTo(null);
}


// Modified initializeLogin() method
private void initializeLogin() {
    // Create login panel
    loginPanel = new JPanel(null);
    loginPanel.setBackground(new Color(255, 255, 205));
    loginPanel.setBounds(0, 0, 1050, 600); // Match frame size
    
    // Title
    JLabel title = new JLabel("FINTRACK ", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 24));
    title.setForeground(new Color(255, 102, 51));
    title.setBounds(350, 50, 300, 30);
    loginPanel.add(title);
    
    // Role selection
    JLabel roleLabel = new JLabel("Select Role:");
    roleLabel.setBounds(350, 120, 100, 25);
    loginPanel.add(roleLabel);
    
    adminRadio = new JRadioButton("Admin");
    userRadio = new JRadioButton("User");
    roleGroup = new ButtonGroup();
    roleGroup.add(adminRadio);
    roleGroup.add(userRadio);
    adminRadio.setBounds(460, 120, 80, 25);
    userRadio.setBounds(540, 120, 80, 25);
    loginPanel.add(adminRadio);
    loginPanel.add(userRadio);
    adminRadio.setSelected(true);
    
    // Username field
    JLabel userLabel = new JLabel("Username:");
    userLabel.setBounds(350, 160, 100, 25);
    loginPanel.add(userLabel);
    
    loginUsernameField = new JTextField();
    loginUsernameField.setBounds(460, 160, 150, 25);
    loginPanel.add(loginUsernameField);
    
    // Password field
    JLabel passLabel = new JLabel("Password:");
    passLabel.setBounds(350, 200, 100, 25);
    loginPanel.add(passLabel);
    
    loginPasswordField = new JPasswordField();
    loginPasswordField.setBounds(460, 200, 150, 25);
    loginPanel.add(loginPasswordField);
    
    // Login button
    JButton loginButton = new JButton("Login");
    loginButton.setBounds(460, 250, 150, 30);
    loginButton.addActionListener(e -> validateLogin());
    loginPanel.add(loginButton);
    
    // Add login panel to frame
    add(loginPanel);
}

// Modified validateLogin() method
private void validateLogin() {
    String username = loginUsernameField.getText();
    String password = new String(loginPasswordField.getPassword());
    
    // Authentication check
    if ((adminRadio.isSelected() && username.equals("admin") && password.equals("admin123"))) {
        userRole = "admin";
        loginPanel.setVisible(false);
        leftPanel.setVisible(true);
        topPanel.setVisible(true);
        mainContentPanel.setVisible(true);
        
        // Set user info
        userInfoLabel.setText("Logged in as: Admin");
        signoutButton.setVisible(true);
    } 
    else if ((userRadio.isSelected() && username.equals("user") && password.equals("user123"))) {
        userRole = "user";
        loginPanel.setVisible(false);
        leftPanel.setVisible(true);
        topPanel.setVisible(true);
        mainContentPanel.setVisible(true);
        
        // Set user info
        userInfoLabel.setText("Logged in as: User");
        signoutButton.setVisible(true);
        applyUserRestrictions();
    }
    else {
        JOptionPane.showMessageDialog(this, 
            "Invalid username/password", 
            "Login Failed", 
            JOptionPane.ERROR_MESSAGE);
    }
}

private void applyUserRestrictions() {
    if ("user".equals(userRole)) {
        // Disable only the Employee menu for regular users
        empMenu.setEnabled(false);
        
        // Additional security - hide Employee options if they were previously shown
        if ("Employee".equals(currentMode)) {
            hideOptionButtons();
            cardLayout.show(mainContentPanel, "HomePanel");
        }
    }
    
    // These can remain enabled for both roles
    homeMenu.setEnabled(true);
    clientsMenu.setEnabled(true);
    eventsMenu.setEnabled(true);
    
    // Enable all option buttons (they'll be managed when each menu is accessed)
    option1.setEnabled(true);
    option2.setEnabled(true);
    option3.setEnabled(true);
    option4.setEnabled(true);
    option5.setEnabled(true);
    option6.setEnabled(true);
    
    // Make sure signout button is visible for users too
    signoutButton.setVisible(true);
}
   private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4));
        panel.setPreferredSize(new Dimension(0, 80));
        panel.setBackground(new Color(255, 102, 51));

        homeMenu = createMenuButton("Home", e -> showPanel("HomePanel"));
        empMenu = createMenuButton("Employee", e -> showOptionButtons("Employee"));
        clientsMenu = createMenuButton("Clients", e -> showOptionButtons("Clients"));
       // leadsMenu = createMenuButton("Leads", e -> showOptionButtons("Leads"));
        // eventsMenu = createMenuButton("Events", e -> showPanel("EventPanel"));
        eventsMenu = createMenuButton("Events", e -> showOptionButtons("Events"));

        panel.add(homeMenu);
        panel.add(empMenu);
        panel.add(clientsMenu);
        //panel.add(leadsMenu);
        panel.add(eventsMenu);
        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBackground(new Color(255,255,230));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    
        // Title at the top
        title = new JLabel("FINTRACK", SwingConstants.CENTER);
        title.setFont(new Font("Aerial", Font.BOLD, 30));
        title.setForeground(new Color(255, 102, 51));
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        title.setBounds(3, 10, 244, 40);
        panel.add(title);
    
        // Option buttons in the middle
        option1 = createOptionButton("");
        option2 = createOptionButton("");
        option3 = createOptionButton("");
        option4 = createOptionButton("");
        option5 = createOptionButton("");
        option6 = createOptionButton("");
    
        option1.setBounds(3, 60, 244, 40);  // Adjusted positions
        option2.setBounds(3, 100, 244, 40);
        option3.setBounds(3, 140, 244, 40);
        option4.setBounds(3, 180, 244, 40);
        option5.setBounds(3, 220, 244, 40);
        option6.setBounds(3, 260, 244, 40);
    
        panel.add(option1);
        panel.add(option2);
        panel.add(option3);
        panel.add(option4);
        panel.add(option5);
        panel.add(option6);
        hideOptionButtons();
    
        // User info and signout at the bottom
        userInfoLabel = new JLabel("", SwingConstants.CENTER);
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userInfoLabel.setBounds(3, 320, 244, 20);  // Positioned near bottom
        panel.add(userInfoLabel);
    
        signoutButton = new JButton("Sign Out");
        signoutButton.setBounds(70, 350, 100, 30);  // Positioned below user info
        signoutButton.addActionListener(e -> signOut());
        signoutButton.setVisible(false);
        panel.add(signoutButton);
    
        return panel;
    }

    private void signOut() {
        // Show login panel
        loginPanel.setVisible(true);
        loginUsernameField.setText("");
        loginPasswordField.setText("");
        adminRadio.setSelected(true);
        
        // Hide main application panels
        leftPanel.setVisible(false);
        topPanel.setVisible(false);
        mainContentPanel.setVisible(false);
        
        // Reset user info and UI state
        userInfoLabel.setText("");
        signoutButton.setVisible(false);
        
        // Reset Employee menu to default state
        empMenu.setEnabled(true);
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
        

        clearActionListeners(option1, option2, option3, option4, option5, option6);
        mainContentPanel.removeAll();
    mainContentPanel.revalidate();
    mainContentPanel.repaint();
        switch (mode) {
            case "Employee" -> {
                option1.setText("Attendance");
                option2.setText("View Employees");
                option3.setText("Add New Employee");
                option4.setText("Update Employee");
                //option5.setText("Delete Employee");
                //option6.setText("Payroll");

                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                option4.setVisible(true);
                option5.setVisible(false);
                option6.setVisible(false);
                
                option1.addActionListener(e -> openEmployeeAttendance());
                option2.addActionListener(e -> openViewEmployee());
                option3.addActionListener(e -> openAddNewEmployee());
                option4.addActionListener(e -> openUpdateEmployee());
                // option5.addActionListener(e -> System.out.println("Payroll"));
                // option6.addActionListener(e -> System.out.println("Payroll"));
            }
            case "Clients" -> {
                option1.setText("Update Project");
                option2.setText("Add Client/Project");
                option3.setText("Update Client Details"); 
                option4.setText("Delete Client");

                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                option4.setVisible(true);
                option5.setVisible(false); // Not needed for Clients
                option6.setVisible(false); // Not needed for Clients

                option1.addActionListener(e -> OpenProject());
                option2.addActionListener(e -> OpenAddNewClient());
                option3.addActionListener(e -> OpenUpdateClientDetails());
                option4.addActionListener(e -> OpenDeleteClient());
            }
            case "Events" -> {
                option1.setText("View Events");
                option2.setText("Add New Event");
                option3.setText("Update Exisiting event");
                option4.setText("Delete Event");

                // Make only relevant buttons visible
                option1.setVisible(true);
                option2.setVisible(true);
                option3.setVisible(true);
                option4.setVisible(true);
                option5.setVisible(false); // Hide unused buttons
                option6.setVisible(false); // Hide unused buttons

                // Assign action listeners
                option1.addActionListener(e -> OpenViewEvent());
                option2.addActionListener(e -> OpenAddNewEvent());
                option3.addActionListener(e -> OpenUpdateEvent());
                option4.addActionListener(e -> OpenDeleteEvent());
            }
        }

       
    }
    private void clearActionListeners(JButton... buttons) {
        for (JButton button : buttons) {
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
        }
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

    private void openViewEmployee() {
        mainContentPanel.removeAll();
        ViewEmployee viewEmployee = new ViewEmployee();
        mainContentPanel.add(viewEmployee.getPanel(), "ViewEmployee");
        cardLayout.show(mainContentPanel, "ViewEmployee");
    }

    private void openAddNewEmployee() {
        AddNewEmployee addNewEmployee = new AddNewEmployee();
        mainContentPanel.add(addNewEmployee.getPanel(), "AddNewEmployee");
        cardLayout.show(mainContentPanel, "AddNewEmployee");
    }

    private void openUpdateEmployee() {
        mainContentPanel.removeAll();
        updateEmployee updateEmployee = new updateEmployee();
        mainContentPanel.add(updateEmployee.getPanel(), "UpdateEmployee");
        cardLayout.show(mainContentPanel, "UpdateEmployee");
    }

    // private void openDeleteEmployee()
    // {
    //     mainContentPanel.removeAll();
    //     deleteEmployee delEmployee = new deleteEmployee();
    //     mainContentPanel.add(delEmployee.getPanel(), "DeleteEmployee");
    //     cardLayout.show(mainContentPanel, "DeleteEmployee");
    // }

    private void OpenAddNewClient(){
       
        mainContentPanel.removeAll();
        AddNewClient addNewClient = new AddNewClient();
        mainContentPanel.add(addNewClient.getPanel(), "AddNewClient");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "AddNewClient");

    }

    private void OpenProject(){
        mainContentPanel.removeAll();
        project newProject = new project();
        mainContentPanel.add(newProject.getPanel(), "NewProject");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "NewProject");
    }

    private void OpenUpdateClientDetails(){
        mainContentPanel.removeAll();
        updateClientDetails updateClient = new updateClientDetails();
        mainContentPanel.add(updateClient.getPanel(), "UpdateClient");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "UpdateClient");
       
    }

    private void OpenDeleteClient()
    {
        mainContentPanel.removeAll();
        deleteClient delClient=new deleteClient();
        mainContentPanel.add(delClient.getPanel(),"DeleteClient");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel,"DeleteClient");
    }

    private void OpenViewEvent(){
        mainContentPanel.removeAll();
        viewEvent viewEvent = new viewEvent();
        mainContentPanel.add(viewEvent.getPanel(), "ViewEvent");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "ViewEvent");
    }
    private void OpenAddNewEvent(){
        mainContentPanel.removeAll();
        addEvent addEvent = new addEvent();
        mainContentPanel.add(addEvent.getPanel(), "AddNewEvent");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "AddNewEvent");
    }

    private void OpenUpdateEvent(){
        mainContentPanel.removeAll();
        updateEvent updateEvent = new updateEvent();
        mainContentPanel.add(updateEvent.getPanel(), "UpdateEvent");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "UpdateEvent");
    }
    private void OpenDeleteEvent(){
        mainContentPanel.removeAll();
        deleteEvent deleteEvent = new deleteEvent();
        mainContentPanel.add(deleteEvent.getPanel(), "DeleteEvent");
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        cardLayout.show(mainContentPanel, "DeleteEvent");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}
