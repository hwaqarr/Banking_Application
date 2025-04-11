import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AdminFrame extends JFrame implements ActionListener{ // Each of the GUI classes will extend JFrame and most that have button interactable will implement ActionListener interface to allow for inputs
    
    ImageIcon image = new ImageIcon("icon.png");
    JSONArray recordsList;
    static JComboBox recordsBoxes;
    JButton confirmButton;

    AdminFrame(JSONArray records) { // Constructor

        this.setTitle("Admin Page");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(500, 500); 

        this.recordsList = records;

        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(64, 115, 255));

        configureComponents();
        registerComponents();

        this.setVisible(true);
    }

    public void configureComboBox() {
        recordsBoxes = new JComboBox<>();
        recordsBoxes.addActionListener(this);
        recordsList.forEach( rcd -> addRecordObject( (JSONObject) rcd) );
        recordsBoxes.setBounds(25, 25, 350, 25);
    }

    public void configureButton() {
        confirmButton = new JButton("Confirm");
        confirmButton.setBounds(200, 100, 100, 50);
        confirmButton.addActionListener(this);
        confirmButton.setText("Confirm");
        confirmButton.setFocusable(false);
    }

    public void configureComponents() {
        configureComboBox();
        configureButton();
    }

    public void registerComponents() { 
        this.add(recordsBoxes);
        this.add(confirmButton);
    }
    
    private static void addRecordObject(JSONObject record) 
    {
        recordsBoxes.addItem(record);
    }

    private void parseRecordObject(JSONObject record, String inputAccount) 
    {

        JSONObject recordObject = (JSONObject) record.get(inputAccount);

        if (recordObject != null) {
            new AdminUserFrame(recordObject, this.recordsList, inputAccount);
            this.setVisible(false);
            this.dispose();
        }
         
    }

    @Override
    public void actionPerformed(ActionEvent e){ 
        if (e.getSource()==confirmButton) {
            String item = recordsBoxes.getSelectedItem().toString();
            String[] itemArray = item.split("\"");
            String accountNumber = itemArray[1];
            recordsList.forEach( rcd -> parseRecordObject( (JSONObject) rcd, accountNumber) );
        }
    }

}
