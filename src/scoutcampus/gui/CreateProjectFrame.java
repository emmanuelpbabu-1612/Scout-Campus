package scoutcampus.gui;

import scoutcampus.dao.ProjectDAO;
import scoutcampus.model.Project;
import scoutcampus.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateProjectFrame extends JFrame {

    private final Student student;
    private final StudentDashboard dashboard;

    private final JTextField titleField = new JTextField(25);
    private final JTextArea descriptionArea = new JTextArea(4, 25);
    private final JTextField skillsField = new JTextField(25);
    private final JSpinner teamSizeSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));

    public CreateProjectFrame(Student student, StudentDashboard dashboard) {
        this.student = student;
        this.dashboard = dashboard;

        setTitle("Create Project");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 380);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel header = new JLabel("Post a New Project", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(header, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Project Title:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Required Skills:"), gbc);
        gbc.gridx = 1;
        panel.add(skillsField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Team Size:"), gbc);
        gbc.gridx = 1;
        panel.add(teamSizeSpinner, gbc);

        JButton createBtn = new JButton("Create");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(createBtn, gbc);

        add(panel);

        createBtn.addActionListener(e -> createProject());
    }

    private void createProject() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String skills = skillsField.getText().trim();
        int teamSize = (Integer) teamSizeSpinner.getValue();

        if (title.isEmpty() || skills.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Project title and required skills are mandatory.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Project project = new Project(0, title, description, skills, teamSize, student.getId(), "Open");
        ProjectDAO dao = new ProjectDAO();
        try {
            boolean success = dao.createProject(project);
            if (success) {
                JOptionPane.showMessageDialog(this, "Project posted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                if (dashboard != null) {
                    dashboard.refreshStats();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to post project.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
