import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserFrame extends JFrame implements ActionListener{ // Each of the GUI classes will extend JFrame and most that have button interactable will implement ActionListener interface to allow for inputs

    ImageIcon image = new ImageIcon("icon.png");
    JButton transferButton;
    JButton payCardButton;
    JButton displayButton;
    JTextField transferTextField;
    JTextField payCardTextField;
    JSONArray recordsList;
    JSONObject currentRecord;
    static String currentAccountNumber;
    JLabel errorLabel;
    JLabel currentDebtLabel;
    static JComboBox recordsBoxes;
    JSONObject transferRecord;

    UserFrame(JSONObject record, JSONArray records, String accountNumber) { // Constructor

        this.setTitle("User Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500); 

        this.recordsList = records;
        this.currentRecord = record;
        UserFrame.currentAccountNumber = accountNumber;

        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(64, 115, 255));

        configureComponents();
        registerComponents();

        this.setVisible(true);
    }

    public void configureButtons() {
        transferButton = new JButton("Transfer");
        transferButton.setBounds(50, 50, 100, 50);
        transferButton.addActionListener(this);
        transferButton.setFocusable(false);
        payCardButton = new JButton("Pay Card");
        payCardButton.setBounds(50, 125, 100, 50);
        payCardButton.addActionListener(this);
        transferButton.setFocusable(false);
        displayButton = new JButton("Display");
        displayButton.setBounds(50, 200, 100, 50);
        displayButton.addActionListener(this);
        transferButton.setFocusable(false);
    }
    
    public void configureTextFields() {
        transferTextField = new JTextField();
        payCardTextField = new JTextField();
        transferTextField.setBounds(200, 50, 100, 25);
        payCardTextField.setBounds(200, 125, 100, 25);
        payCardTextField.setText((String) currentRecord.get("debt"));
        payCardTextField.setEditable(false);
        payCardTextField.setFocusable(false);
    }

    public void configureLabels() {
        errorLabel = new JLabel();
        currentDebtLabel = new JLabel("Current Debt");
        errorLabel.setBounds(200, 200, 250, 25);
        currentDebtLabel.setBounds(200, 100, 100, 25);
    }

    public void configureComponents() {
        configureButtons();
        configureTextFields();
        configureLabels();
        configureComboBox();
    }

    public void configureComboBox() {
        recordsBoxes = new JComboBox<>();
        recordsBoxes.addActionListener(this);
        recordsList.forEach( rcd -> addRecordObject( (JSONObject) rcd) );
        recordsBoxes.setBounds(325, 50, 150, 25);
    }

    public void registerComponents() {
        this.add(transferButton);
        this.add(payCardButton);
        this.add(displayButton);
        this.add(transferTextField);
        this.add(payCardTextField);
        this.add(errorLabel);
        this.add(recordsBoxes);
        this.add(currentDebtLabel);
    }

    public void writeToFile() {
        try (FileWriter file = new FileWriter("records.json")) {
            file.write(this.recordsList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addRecordObject(JSONObject record) 
    {   
        JSONObject recordObject = (JSONObject) record.get(UserFrame.currentAccountNumber);
        if (recordObject == null) {
            recordsBoxes.addItem(record);
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

    private void findRecordObject(JSONObject record, String inputAccount) 
    {

        JSONObject recordObject = (JSONObject) record.get(inputAccount);

        if (recordObject != null) {
            this.transferRecord = recordObject;
        }
         
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == transferButton) {
            if (isNumeric(transferTextField.getText())) {
                String item = recordsBoxes.getSelectedItem().toString();
                String[] itemArray = item.split("\"");
                String transferAccountNumber = itemArray[1];
                recordsList.forEach( rcd -> findRecordObject( (JSONObject) rcd, transferAccountNumber) );
                
                Integer transferRecordBalance = Integer.valueOf((String) this.transferRecord.get("balance"));
                Integer currentRecordBalance = Integer.valueOf((String) this.currentRecord.get("balance"));
                Integer transferAmount = Integer.valueOf(transferTextField.getText());

                if (currentRecordBalance >= transferAmount) {
                    String transferRecordUpdatedBalance = String.valueOf(transferRecordBalance + transferAmount);
                    this.transferRecord.put("balance", transferRecordUpdatedBalance);
                    String currentAccountUpdatedBalance = String.valueOf(currentRecordBalance - transferAmount);
                    this.currentRecord.put("balance", currentAccountUpdatedBalance);
                    
                    writeToFile();
                }
                else {
                    errorLabel.setText("Error: Insufficent Funds");
                }
            }
            else {
                errorLabel.setText("Error: Enter a numerical value to transfer");
            }
        }
        else if (e.getSource() == payCardButton) {
            if (isNumeric(payCardTextField.getText())) {
                Integer currentRecordBalance = Integer.valueOf((String) this.currentRecord.get("balance"));
                Integer currentRecordDebt = Integer.valueOf((String) this.currentRecord.get("debt"));
                String updatedBalance = "0";
                String updatedDebt = "0";
                
                if (currentRecordBalance >= currentRecordDebt) {
                    updatedBalance = String.valueOf(currentRecordBalance - currentRecordDebt);
                    updatedDebt = "0";
                }
                else {
                    updatedBalance = "0";
                    updatedDebt = String.valueOf(currentRecordDebt - currentRecordBalance);
                }
                this.currentRecord.put("balance", updatedBalance);
                this.currentRecord.put("debt", updatedDebt);
                writeToFile();
                payCardTextField.setText((String) currentRecord.get("debt"));
            }
            else {
                errorLabel.setText("Error: Enter a number to pay card");
            }
        }
        else if (e.getSource() == displayButton) {
            new AccountDetailsFrame(this.currentRecord, this.currentAccountNumber); 
        }
        
    }

}
