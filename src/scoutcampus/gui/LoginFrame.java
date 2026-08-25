package scoutcampus.gui;

import scoutcampus.dao.StudentDAO;
import scoutcampus.model.Admin;
import scoutcampus.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JCheckBox adminCheck = new JCheckBox("Login as Admin");

    public LoginFrame() {
        setTitle("Scout Campus");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("SCOUT CAMPUS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel subtitle = new JLabel("Student Startup & Project Collaboration Platform", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 1;
        panel.add(adminCheck, gbc);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        loginBtn.addActionListener(e -> attemptLogin());
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StudentDAO dao = new StudentDAO();
        try {
            if (adminCheck.isSelected()) {
                Admin admin = dao.loginAdmin(email, password);
                if (admin != null) {
                    admin.displayDashboard();
                    new AdminDashboard(admin).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Student student = dao.login(email, password);
                if (student != null) {
                    student.displayDashboard();
                    new StudentDashboard(student).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            // EXCEPTION HANDLING: database errors are caught and shown to the user
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
