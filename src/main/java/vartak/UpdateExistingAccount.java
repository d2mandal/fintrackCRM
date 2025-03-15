package vartak;

import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

public class UpdateExistingAccount{
    JPanel panel;
    public UpdateExistingAccount(JPanel mainContentPanel,CardLayout layout){
        panel=new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);

        //Page heading
        JLabel title=new JLabel("Update Existing Account : ");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.black);
        title.setBounds(20, 20, 350, 30);
        panel.add(title);

        //Account Number checking
        JLabel titleLabel = new JLabel("Account Number:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        titleLabel.setBounds(20, 20, 300, 30);
        panel.add(titleLabel);

        JTextField accountNumber = new JTextField();
        //accountNumber.setBounds(180, 20, 200, 25);
        panel.add(accountNumber);
       


    }
    public JPanel getPanel(){
        
        return panel;
    }
}