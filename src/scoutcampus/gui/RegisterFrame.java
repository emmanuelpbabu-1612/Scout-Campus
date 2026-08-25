package scoutcampus.gui;

import scoutcampus.dao.StudentDAO;
import scoutcampus.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField departmentField = new JTextField(20);
    private final JComboBox<String> yearBox = new JComboBox<>(
            new String[]{"1st Year", "2nd Year", "3rd Year", "4th Year"});
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);

    public RegisterFrame() {
        setTitle("Scout Campus - Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 420);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Your Scout Campus Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        addRow(panel, gbc, 1, "Name:", nameField);
        addRow(panel, gbc, 2, "Email:", emailField);
        addRow(panel, gbc, 3, "Department:", departmentField);
        addRow(panel, gbc, 4, "Year:", yearBox);
        addRow(panel, gbc, 5, "Password:", passwordField);
        addRow(panel, gbc, 6, "Confirm Password:", confirmField);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        registerBtn.addActionListener(e -> attemptRegister());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void attemptRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String department = departmentField.getText().trim();
        String year = (String) yearBox.getSelectedItem();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (name.isEmpty() || email.isEmpty() || department.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Password Mismatch", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student s = new Student(0, name, email, password, department, year);
        StudentDAO dao = new StudentDAO();
        try {
            boolean success = dao.register(s);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            // EXCEPTION HANDLING: e.g. duplicate email violates UNIQUE constraint
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
