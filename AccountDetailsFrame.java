import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.simple.JSONObject;

public class AccountDetailsFrame extends JFrame implements ActionListener{ // Each of the GUI classes will extend JFrame and most that have button interactable will implement ActionListener interface to allow for inputs
    
    ImageIcon image = new ImageIcon("icon.png");
    JLabel name;
    JLabel accountNumber;
    JLabel balance;
    JLabel password;
    JLabel debt;
    JSONObject currentRecord;
    JButton closeButton;
    String currentAccountNumber;

    AccountDetailsFrame(JSONObject record, String accountNumber) {
        this.setTitle("Account Details");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500); 

        this.currentRecord = record;
        this.currentAccountNumber = accountNumber;

        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(64, 115, 255));

        configureComponents();
        registerComponents();

        this.setVisible(true);
    }

    public void configureLabels(){
        name = new JLabel("Name: " + (String) this.currentRecord.get("name"));
        accountNumber = new JLabel("Account Number: " + this.currentAccountNumber);
        password = new JLabel("Password: " + (String) this.currentRecord.get("password"));
        balance = new JLabel("Balance: " + (String) this.currentRecord.get("balance"));
        debt = new JLabel("Debt: " + (String) this.currentRecord.get("debt"));

        name.setBounds(50, 50, 125, 25);
        accountNumber.setBounds(50, 100, 175, 25);
        password.setBounds(50, 150, 125, 25);
        balance.setBounds(50, 200, 125, 25);
        debt.setBounds(50, 250, 125, 25);
    }

    public void configureButton() {
        closeButton = new JButton("Exit");
        closeButton.setBounds(300, 200, 100, 50);
        closeButton.addActionListener(this);
        closeButton.setFocusable(false);
    }

    public void configureComponents() {
        configureLabels();
        configureButton();
    }

    public void registerComponents(){
        this.add(name);
        this.add(balance);
        this.add(password);
        this.add(accountNumber);
        this.add(closeButton);
        this.add(debt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        this.dispose();
    }

}
