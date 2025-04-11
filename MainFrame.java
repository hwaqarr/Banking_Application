import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MainFrame extends JFrame implements ActionListener{ // Each of the GUI classes will extend JFrame and most that have button interactable will implement ActionListener interface to allow for inputs

    JButton confirmButton; // Declaring the components of the frame
    JButton createAccount;
    JTextField accountNumberTextField;
    JTextField accountPasswordTextField;
    JLabel accountEnterLabel;
    JLabel passwordEnterLabel;
    JLabel errorMessageLabel;
    ImageIcon image = new ImageIcon("icon.png"); // Declaring + Initializing image icon
    JSONArray recordsList;

    MainFrame(JSONArray records) { // Frame constructor
        
        this.setTitle("Main Page"); // Frame setup
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500); 
        this.recordsList = records;

        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(64, 115, 255));

        configureComponents(); // Configure components
        registerComponents(); // Add components to frame

        this.setVisible(true);
    }
    
    public void registerComponents() { // Helper function to add all components to JFrame
        this.add(confirmButton);
        this.add(createAccount);
        this.add(accountNumberTextField);
        this.add(accountPasswordTextField);
        this.add(accountEnterLabel);
        this.add(passwordEnterLabel);
        this.add(errorMessageLabel);
    }

    public void configureComponents() { // Helper function to configure all the components
        configureButtons();
        configureTextField();
        configureLabels();
    }

    public void configureButtons() { // Helper function to configure all buttons
        confirmButton = new JButton();
        confirmButton.setBounds(150, 350, 100, 50);
        confirmButton.addActionListener(this);
        confirmButton.setText("Login");
        confirmButton.setFocusable(false);

        createAccount = new JButton();
        createAccount.setBounds(250, 350, 100, 50);
        createAccount.addActionListener(this);
        createAccount.setText("Create");
        createAccount.setFocusable(false);
    }

    public void configureTextField() { // Helper function to configure all text fields
        accountNumberTextField = new JTextField();
        accountPasswordTextField = new JTextField();
        accountNumberTextField.setBounds(125, 200, 250, 20);
        accountPasswordTextField.setBounds(125, 250, 250, 20);
    }

    public void configureLabels() { // Helper function to configure all labels
        accountEnterLabel = new JLabel();
        passwordEnterLabel = new JLabel();
        errorMessageLabel = new JLabel();

        accountEnterLabel.setBounds(50, 200, 75, 20);
        accountEnterLabel.setText("Account:");
        passwordEnterLabel.setBounds(50, 250, 75, 20);
        passwordEnterLabel.setText("Password:");
        errorMessageLabel.setBounds(100, 300, 250, 25);
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Function override to take input from the JFrame
        if (e.getSource()==confirmButton) {
            if (accountNumberTextField.getText().equals("admin") && accountPasswordTextField.getText().equals("Beans")) {
                new AdminFrame(recordsList);
                this.setVisible(false);
                this.dispose();
            }
            else {
                errorMessageLabel.setText("");
                String account = accountNumberTextField.getText();
                recordsList.forEach( rcd -> parseRecordObject( (JSONObject) rcd , account) );
                if (errorMessageLabel.getText().equals("")) { // Error handling, if account is not found in list using parseRecordObject and forEach loop then display error
                    errorMessageLabel.setText("Account Not In Records"); 
                }
            }
        }
        else if (e.getSource()==createAccount) {
            new AccountCreateFrame(this.recordsList);
        }
    }

    private void parseRecordObject(JSONObject record, String inputAccount) // Helper function to iterate through each individual JSONObject record
    {

        JSONObject recordObject = (JSONObject) record.get(inputAccount);

        if (recordObject != null) {
            String passwordContents = (String) recordObject.get("password");
            String inputPassword = accountPasswordTextField.getText();

            if (passwordContents.equals(inputPassword)) { // If the JSONObject is found in the array
                new UserFrame(recordObject, this.recordsList, inputAccount); // Open up User GUI and pass in the information of the login information user input
                this.setVisible(false); // Hides and closes frame
                this.dispose();
            }
            else { // Error handling for incorrect password
                errorMessageLabel.setText("Password Incorrect");
            }
        }
         
    }

}
