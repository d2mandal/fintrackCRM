package vartak;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class event {
    private JPanel eventPanel;

    public event() {
        eventPanel = new JPanel(new BorderLayout());
        eventPanel.setPreferredSize(new Dimension(400, 600));
        
        // Title Bar
        JPanel titleBar = new JPanel();
        titleBar.setPreferredSize(new Dimension(400, 80));
        titleBar.setBackground(new Color(252, 221, 176));
        JLabel titleText = new JLabel("To Do List");
        titleText.setFont(new Font("Sans-serif", Font.BOLD, 20));
        titleBar.add(titleText);
        eventPanel.add(titleBar, BorderLayout.NORTH);
        
        // Task List
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(10, 1, 5, 5));
        listPanel.setBackground(new Color(252, 221, 176));
        JScrollPane scrollPane = new JScrollPane(listPanel);
        eventPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(400, 60));
        footer.setBackground(new Color(252, 221, 176));
        
        JButton addTask = new JButton("Add Task");
        JButton clear = new JButton("Clear Finished Tasks");
        footer.add(addTask);
        footer.add(Box.createHorizontalStrut(20));
        footer.add(clear);
        eventPanel.add(footer, BorderLayout.SOUTH);
        
        // Add Task Button Logic
        addTask.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPanel taskPanel = new JPanel(new BorderLayout());
                taskPanel.setBackground(new Color(255, 161, 161));
                
                JLabel indexLabel = new JLabel((listPanel.getComponentCount() + 1) + "");
                indexLabel.setPreferredSize(new Dimension(20, 20));
                indexLabel.setHorizontalAlignment(JLabel.CENTER);
                taskPanel.add(indexLabel, BorderLayout.WEST);
                
                JTextField taskName = new JTextField("Write something..");
                taskName.setBackground(new Color(255, 161, 161));
                taskPanel.add(taskName, BorderLayout.CENTER);
                
                JButton done = new JButton("Done");
                done.setBackground(new Color(233, 119, 119));
                taskPanel.add(done, BorderLayout.EAST);
                
                listPanel.add(taskPanel);
                listPanel.revalidate();
                
                done.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        taskPanel.setBackground(new Color(188, 226, 158));
                        taskName.setBackground(new Color(188, 226, 158));
                    }
                });
            }
        });
        
        // Clear Button Logic
        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Component[] components = listPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JPanel) {
                        JPanel taskPanel = (JPanel) component;
                        if (taskPanel.getBackground().equals(new Color(188, 226, 158))) {
                            listPanel.remove(taskPanel);
                        }
                    }
                }
                listPanel.revalidate();
                listPanel.repaint();
            }
        });
    }
    
    public JPanel getPanel() {
        return eventPanel;
    }
}