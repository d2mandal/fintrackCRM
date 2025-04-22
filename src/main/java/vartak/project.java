package vartak;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class project {

    class ProjectData {
        String ClientName;
        String mail;
        String mob;
        String projectName;
        String projectManager;
        String status;
        String dateAdded;
        String deadline;
        String projDesc;
        String updateDate;
        String updateNotes;

        public ProjectData(String ClientName,String mail,String mob,String projectName,String projectManager,String status,String dateAdded,String deadline,String projDesc,String updateDate,String updateNotes) 
        {
            this.ClientName = ClientName;
            this.mail = mail;
            this.mob = mob;
            this.projectName = projectName;
            this.projectManager = projectManager;
            this.status = status;
            this.dateAdded = dateAdded;
            this.deadline = deadline;
            this.projDesc = projDesc;
            this.updateDate = updateDate;
            this.updateNotes = updateNotes;
           
        }
    }

    private JPanel newProjectPanel;
    private JTable projectTable;
    private JComboBox<String> statusFilter;
    private DefaultTableModel tableModel;
    private JButton addProject;
    private Map<Integer, ProjectData> projectDataMap = new HashMap<>();

    public project() {
        newProjectPanel = new JPanel(null);
        newProjectPanel.setBorder(BorderFactory.createTitledBorder("View Project"));
        newProjectPanel.setBackground(new Color(255, 255, 204));

        JLabel filterLabel = new JLabel("Filter :");
        filterLabel.setBounds(20, 20, 100, 25);
        newProjectPanel.add(filterLabel);

        statusFilter = new JComboBox<>(new String[]{"All", "Planned", "Ongoing", "Completed", "onHold", "Terminated"});
        statusFilter.setBounds(80, 20, 100, 25);
        newProjectPanel.add(statusFilter);

        addProject = new JButton("Add New Project");
        addProject.setBounds(600, 20, 150, 25);
        newProjectPanel.add(addProject);

        addProject.addActionListener(e -> JOptionPane.showMessageDialog(newProjectPanel, "Add New Project functionality not implemented yet."));

        String[] columns = {"Project Name", "Deadline", "Status", "Progress", "Action"};
        tableModel = new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };

        fetchProjectsFromDatabase();

        projectTable = new JTable(tableModel);
        projectTable.getColumn("Progress").setCellRenderer(new ButtonRenderer());
        projectTable.getColumn("Progress").setCellEditor(new ButtonEditor(new JCheckBox()));

        projectTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        projectTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(projectTable);
        scrollPane.setBounds(10, 50, 780, 500);
        newProjectPanel.add(scrollPane);

        statusFilter.addActionListener(e -> filterProjects());
    }

    public JPanel getPanel() {
        return newProjectPanel;
    }

    private void fetchProjectsFromDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ClientName,mail,mob,projectName,projectManager,status,dateAdded,deadline,projectDesc,updateNotes,updateDate FROM clients");

            int rowIndex = 0;
            while (rs.next()) {
                String ClientName = rs.getString("ClientName");
                String mail=rs.getString("mail");
                String mob=rs.getString("mob");
                String ProjectName=rs.getString("projectName");
                String projectmanager = rs.getString("projectManager");
                String Status = rs.getString("status");
                String dateAdded=rs.getString("dateAdded");
                String DeadLine = rs.getString("deadLine");
                String projectDesc= rs.getString("projectDesc");
                String updateNotes = rs.getString("updateNotes");
                String updateDate = rs.getString("updateDate");
                ProjectData data = new ProjectData(ClientName,mail,mob,ProjectName,projectmanager,Status,dateAdded, DeadLine, projectDesc,updateNotes,updateDate);
                projectDataMap.put(rowIndex, data);
                tableModel.addRow(new Object[]{ProjectName,DeadLine, Status, "View", "Update"});
                rowIndex++;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(newProjectPanel, "Database Error: " + e.getMessage());
        }
    }

    private void filterProjects() {
        String selected = (String) statusFilter.getSelectedItem();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 2);
            boolean match = selected.equals("All") || status.equals(selected);
            projectTable.setRowHeight(i, match ? 16 : 0);
        }
    }

    private void showViewDialog(ProjectData data) {
        JDialog viewDialog = new JDialog();
        viewDialog.setTitle("View Project - " + data.projectName);
        viewDialog.setSize(800, 400);
        viewDialog.setLayout(null);
        viewDialog.setLocationRelativeTo(null);

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setText(
            "Project Name: " + data.projectName + "\n" +
            "Project Manager: " + data.projectManager + "\n" +
            "Date Added: " + data.dateAdded + "\n" +
            "Deadline: " + data.deadline + "\n" +
            "Status: " + data.status + "\n" +
            "Last Updated: " + data.updateDate + "\n\n" +
            "Description:\n" + data.projDesc + "\n\n" +
            "Update Notes:\n" + data.updateNotes
        );

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setBounds(20, 20, 740, 300);
        viewDialog.add(infoScroll);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(340, 330, 100, 25);
        closeButton.addActionListener(e -> viewDialog.dispose());
        viewDialog.add(closeButton);

        viewDialog.setVisible(true);
    }

    private void showUpdateDialog(ProjectData data)
{
    JDialog updateDialog = new JDialog();
    updateDialog.setTitle("Update Project - " + data.projectName);
    updateDialog.setSize(800, 600);
    updateDialog.setLayout(null);
    updateDialog.setLocationRelativeTo(null);

    JTextArea infoArea = new JTextArea();
    infoArea.setEditable(false);
    infoArea.setText(
        "Project: " + data.projectName + "\n" +
        "Manager: " + data.projectManager + "\n" +
        "Deadline: " + data.deadline + "\n" +
        "Status: " + data.status + "\n" +
        "Last Updated: " + data.updateDate + "\n\n" +
        "Update Notes:\n" + data.updateNotes
    );
    JScrollPane infoScroll = new JScrollPane(infoArea);
    infoScroll.setBounds(20, 20, 740, 300);
    updateDialog.add(infoScroll);

    JLabel commentLabel = new JLabel("New Comment:");
    commentLabel.setBounds(20, 340, 100, 25);
    updateDialog.add(commentLabel);

    JTextField commentField = new JTextField();
    commentField.setBounds(120, 340, 500, 25);
    updateDialog.add(commentField);

    JLabel statusLabel = new JLabel("Update Status:");
    statusLabel.setBounds(20, 380, 100, 25);
    updateDialog.add(statusLabel);

    JComboBox<String> statusBox = new JComboBox<>(new String[]{"Planned", "Ongoing", "Completed", "OnHold", "Terminated"});
    statusBox.setSelectedItem(data.status);
    statusBox.setBounds(120, 380, 150, 25);
    updateDialog.add(statusBox);

    JButton submitButton = new JButton("Submit");
    submitButton.setBounds(180, 430, 100, 30);
    updateDialog.add(submitButton);

    submitButton.addActionListener(e -> {
        String newComment = commentField.getText().trim();
        String newStatus = (String) statusBox.getSelectedItem();

        if (!newComment.isEmpty()) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fintrackdbs", "root", "1234");

                // Current timestamp
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                String newEntry = timestamp + " - " + newComment;

                // Fetch existing notes
                PreparedStatement fetchStmt = conn.prepareStatement("SELECT updateNotes FROM clients WHERE projectName = ?");
                fetchStmt.setString(1, data.projectName); 
                ResultSet rs = fetchStmt.executeQuery();

                String oldNotes = "";
                if (rs.next()) {
                    oldNotes = rs.getString("updateNotes");
                }

                // Prepend new note
                String combinedNotes = newEntry + "\n---\n" + (oldNotes != null ? oldNotes : "");

                // Update database
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE clients SET updateNotes = ?, status = ?, updateDate = ? WHERE projectName = ?");
                updateStmt.setString(1, combinedNotes);
                updateStmt.setString(2, newStatus);
                updateStmt.setString(3, timestamp);
                updateStmt.setString(4, data.projectName);
                updateStmt.executeUpdate();

                conn.close();

                // Update local data object
                data.updateNotes = combinedNotes;
                data.status = newStatus;
                data.updateDate = timestamp;

                // Refresh infoArea text
                infoArea.setText(
                    "Project: " + data.projectName + "\n" +
                    "Manager: " + data.projectManager + "\n" +
                    "Deadline: " + data.deadline + "\n" +
                    "Status: " + data.status + "\n" +
                    "Last Updated: " + data.updateDate + "\n\n" +
                    "Update Notes:\n" + data.updateNotes
                );

                JOptionPane.showMessageDialog(updateDialog, "Update submitted for " + data.projectName);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(updateDialog, "Error while updating project.");
            }
        } else {
            JOptionPane.showMessageDialog(updateDialog, "Please enter a comment before submitting.");
        }
    });

    updateDialog.setVisible(true);
}


       class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int col) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int col) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            clicked = true;
            selectedRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                ProjectData data = projectDataMap.get(selectedRow);
                if (data == null) return label;
                projectTable.setRowSelectionInterval(selectedRow, selectedRow);

                if (label.equals("View")) {
                    showViewDialog(data);
                } else if (label.equals("Update")) {
                    showUpdateDialog(data);
                }
            }
            clicked = false;
            return label;
        }
    }
}

