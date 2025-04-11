import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AdminUserFrame extends JFrame implements ActionListener{ // Each of the GUI classes will extend JFrame and most that have button interactable will implement ActionListener interface to allow for inputs

    ImageIcon image = new ImageIcon("icon.png");
    JButton depositButton;
    JButton withdrawButton;
    JButton displayButton;
    JButton clearDebtButton;
    JTextField depositTextField;
    JTextField withdrawTextField;
    JSONArray recordsList;
    JSONObject currentRecord;
    String currentAccountNumber;
    JLabel errorLabel;

    AdminUserFrame(JSONObject record, JSONArray records, String accountNumber) { // Constructor

        this.setTitle("Admin User Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500); 

        this.recordsList = records;
        this.currentRecord = record;
        this.currentAccountNumber = accountNumber;

        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(64, 115, 255));

        configureComponents();
        registerComponents();

        this.setVisible(true);
    }

    public void configureButtons() {
        depositButton = new JButton("Deposit");
        depositButton.setBounds(50, 50, 100, 50);
        depositButton.addActionListener(this);
        depositButton.setFocusable(false);
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(50, 125, 100, 50);
        withdrawButton.addActionListener(this);
        depositButton.setFocusable(false);
        displayButton = new JButton("Display");
        displayButton.setBounds(50, 200, 100, 50);
        displayButton.addActionListener(this);
        depositButton.setFocusable(false);
        clearDebtButton = new JButton("Clear Debt");
        clearDebtButton.setBounds(50, 275, 100, 50);
        clearDebtButton.addActionListener(this);
        clearDebtButton.setFocusable(false);
    }
    
    public void configureTextFields() {
        depositTextField = new JTextField();
        withdrawTextField = new JTextField();
        depositTextField.setBounds(200, 50, 100, 25);
        withdrawTextField.setBounds(200, 125, 100, 25);
    }

    public void configureLabels() {
        errorLabel = new JLabel();
        errorLabel.setBounds(200, 200, 225, 25);
    }

    public void configureComponents() {
        configureButtons();
        configureTextFields();
        configureLabels();
    }

    public void registerComponents() {
        this.add(depositButton);
        this.add(withdrawButton);
        this.add(displayButton);
        this.add(depositTextField);
        this.add(withdrawTextField);
        this.add(errorLabel);
        this.add(clearDebtButton);
    }

    public void writeToFile() {
        try (FileWriter file = new FileWriter("records.json")) {
            file.write(this.recordsList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            if (isNumeric(depositTextField.getText())) {
                String updatedBalance = String.valueOf(Integer.valueOf(depositTextField.getText()) + Integer.valueOf((String) this.currentRecord.get("balance")));
                this.currentRecord.put("balance", updatedBalance);
                writeToFile();
            }
            else {
                errorLabel.setText("Error: Enter a number to deposit");
            }
        }
        else if (e.getSource() == withdrawButton) {
            if (isNumeric(withdrawTextField.getText())) {
                String updatedBalance = String.valueOf(Integer.valueOf((String) this.currentRecord.get("balance")) - Integer.valueOf(withdrawTextField.getText()));
                this.currentRecord.put("balance", updatedBalance);
                writeToFile();
            }
            else {
                errorLabel.setText("Error: Enter a number to withdraw");
            }
        }
        else if (e.getSource() == displayButton) {
            new AccountDetailsFrame(this.currentRecord, this.currentAccountNumber); 
        }
        else if(e.getSource() == clearDebtButton) {
            this.currentRecord.put("debt", "0");
            writeToFile();
        }
        
    }

}
