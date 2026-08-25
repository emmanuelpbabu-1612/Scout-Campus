package scoutcampus.gui;

import scoutcampus.dao.StudentDAO;
import scoutcampus.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ProfileFrame extends JFrame {

    private final Student student;
    private final JTextArea bioArea = new JTextArea(3, 25);
    private final JTextField skillsField = new JTextField(25);
    private final JTextField interestsField = new JTextField(25);
    private final JTextField githubField = new JTextField(25);
    private final JTextField linkedinField = new JTextField(25);
    private final JTextField startupInterestsField = new JTextField(25);

    public ProfileFrame(Student student) {
        this.student = student;

        setTitle("My Profile - " + student.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 480);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel(student.getName() + " | " + student.getDepartment()
                + " | " + student.getYear());
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(header, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Bio:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(bioArea), gbc);

        addRow(panel, gbc, 2, "Skills (comma separated):", skillsField);
        addRow(panel, gbc, 3, "Interests:", interestsField);
        addRow(panel, gbc, 4, "GitHub URL:", githubField);
        addRow(panel, gbc, 5, "LinkedIn URL:", linkedinField);
        addRow(panel, gbc, 6, "Startup Interests / Looking For:", startupInterestsField);

        JButton saveBtn = new JButton("Save Profile");
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel);
        loadExisting();

        saveBtn.addActionListener(e -> saveProfile());
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadExisting() {
        bioArea.setText(student.getBio());
        skillsField.setText(student.getSkills());
        interestsField.setText(student.getInterests());
        githubField.setText(student.getGithub());
        linkedinField.setText(student.getLinkedin());
        startupInterestsField.setText(student.getStartupInterests());
    }

    private void saveProfile() {
        student.setBio(bioArea.getText().trim());
        student.setSkills(skillsField.getText().trim());
        student.setInterests(interestsField.getText().trim());
        student.setGithub(githubField.getText().trim());
        student.setLinkedin(linkedinField.getText().trim());
        student.setStartupInterests(startupInterestsField.getText().trim());

        StudentDAO dao = new StudentDAO();
        try {
            boolean success = dao.updateProfile(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully.",
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No changes were saved.",
                        "Notice", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
