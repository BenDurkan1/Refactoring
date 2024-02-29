package Refactored;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends JDialog implements ActionListener {
    private JTextField idField, ppsField, surnameField, firstNameField, salaryField;
    private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
    private JButton saveButton, cancelButton;
    private final EmployeeDetails parent;

    public AddRecordDialog(EmployeeDetails parent) {
        super(parent, "Add Record", true);
        this.parent = parent;
        this.parent.setEnabled(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 370);
        setLocationRelativeTo(parent);

        JScrollPane scrollPane = new JScrollPane(createDialogPane());
        setContentPane(scrollPane);

        setVisible(true);
    }
    // Refactored method to create dialog panel
    private Container createDialogPane() {
        JPanel empDetailsPanel = new JPanel(new MigLayout());
        empDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        
        // Changed variable names and organized components
        // Adding components to panel
        idField = new JTextField(20);
        idField.setEditable(false);
        empDetailsPanel.add(new JLabel("ID:"), "growx, pushx");
        empDetailsPanel.add(idField, "growx, pushx, wrap");

        ppsField = new JTextField(20);
        empDetailsPanel.add(new JLabel("PPS Number:"), "growx, pushx");
        empDetailsPanel.add(ppsField, "growx, pushx, wrap");

        surnameField = new JTextField(20);
        empDetailsPanel.add(new JLabel("Surname:"), "growx, pushx");
        empDetailsPanel.add(surnameField, "growx, pushx, wrap");

        firstNameField = new JTextField(20);
        empDetailsPanel.add(new JLabel("First Name:"), "growx, pushx");
        empDetailsPanel.add(firstNameField, "growx, pushx, wrap");

        genderCombo = new JComboBox<>(parent.gender);
        empDetailsPanel.add(new JLabel("Gender:"), "growx, pushx");
        empDetailsPanel.add(genderCombo, "growx, pushx, wrap");

        departmentCombo = new JComboBox<>(parent.department);
        empDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
        empDetailsPanel.add(departmentCombo, "growx, pushx, wrap");

        salaryField = new JTextField(20);
        empDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
        empDetailsPanel.add(salaryField, "growx, pushx, wrap");

        fullTimeCombo = new JComboBox<>(parent.fullTime);
        empDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
        empDetailsPanel.add(fullTimeCombo, "growx, pushx, wrap");

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        empDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");

        return empDetailsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            if (validateInput()) {
                saveRecord();
            }
        } else if (e.getSource() == cancelButton) {
            dispose();
            parent.setEnabled(true);
        }
    }
 // This method checks for empty fields and correct salary format
    private boolean validateInput() {
        boolean valid = true;
        // Check if the text fields are not empty
        if (ppsField.getText().trim().isEmpty() || surnameField.getText().trim().isEmpty() ||
            firstNameField.getText().trim().isEmpty() || salaryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Input Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }

        // Check for valid salary
        try {
            double salary = Double.parseDouble(salaryField.getText());
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a positive number", "Input Error", JOptionPane.ERROR_MESSAGE);
                valid = false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid format for salary", "Input Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }

        return valid;
    }

    // Refactored method to reset text field backgrounds

    private void resetTextFieldBackgrounds() {
        JTextField[] fields = {ppsField, surnameField, firstNameField, salaryField};
        for (JTextField field : fields) {
            field.setBackground(Color.WHITE);
        }
    }

    private void saveRecord() {
        if (!validateInput()) {
            return; // Stop if validation fails
        }

        boolean fullTime = fullTimeCombo.getSelectedIndex() == 1;
        Employee newEmployee = new Employee(parent.getNextFreeId(), ppsField.getText().toUpperCase(),
                surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
                ((String) genderCombo.getSelectedItem()).charAt(0),
                (String) departmentCombo.getSelectedItem(), Double.parseDouble(salaryField.getText()), fullTime);

        // Assuming addRecord in EmployeeDetails is public and can be accessed like this
        parent.addRecord(newEmployee);
        JOptionPane.showMessageDialog(this, "Record added successfully!", "Record Added", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Close the dialog
        parent.setEnabled(true);
        parent.displayRecords(newEmployee);
    }
  
}