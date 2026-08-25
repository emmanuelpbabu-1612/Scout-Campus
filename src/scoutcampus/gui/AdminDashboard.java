package scoutcampus.gui;

import scoutcampus.dao.OpportunityDAO;
import scoutcampus.dao.ProjectDAO;
import scoutcampus.dao.StudentDAO;
import scoutcampus.model.Admin;
import scoutcampus.model.Opportunity;
import scoutcampus.model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {

    private final Admin admin;

    // Post-opportunity fields
    private final JTextField oppTitleField = new JTextField(18);
    private final JComboBox<String> oppTypeBox = new JComboBox<>(
            new String[]{"Hackathon", "Competition", "Workshop", "Startup Event"});
    private final JTextArea oppDescArea = new JTextArea(3, 18);
    private final JTextField oppDeadlineField = new JTextField(10); // yyyy-MM-dd

    // Reports table
    private final DefaultTableModel reportModel = new DefaultTableModel(new String[]{"Metric", "Value"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public AdminDashboard(Admin admin) {
        this.admin = admin;

        setTitle("Scout Campus - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Post Opportunity", buildPostOpportunityPanel());
        tabs.addTab("Campus Reports", buildReportsPanel());

        JPanel top = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("  Welcome, " + admin.getName());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        top.add(welcome, BorderLayout.WEST);
        top.add(logoutBtn, BorderLayout.EAST);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(top, BorderLayout.NORTH);
        main.add(tabs, BorderLayout.CENTER);
        add(main);
    }

    private JPanel buildPostOpportunityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        panel.add(oppTitleField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        panel.add(oppTypeBox, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(oppDescArea), gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Deadline (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        panel.add(oppDeadlineField, gbc);

        JButton postBtn = new JButton("Post Opportunity");
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(postBtn, gbc);

        postBtn.addActionListener(e -> postOpportunity());

        return panel;
    }

    private void postOpportunity() {
        String title = oppTitleField.getText().trim();
        String type = (String) oppTypeBox.getSelectedItem();
        String description = oppDescArea.getText().trim();
        String deadlineText = oppDeadlineField.getText().trim();

        if (title.isEmpty() || deadlineText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and deadline are required.",
                    "Missing Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date deadline;
        try {
            deadline = Date.valueOf(deadlineText); // expects yyyy-MM-dd
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Deadline must be in yyyy-MM-dd format.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Opportunity o = new Opportunity(0, title, type, description, deadline);
        OpportunityDAO dao = new OpportunityDAO();
        try {
            boolean success = dao.create(o);
            if (success) {
                JOptionPane.showMessageDialog(this, "Opportunity posted!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                oppTitleField.setText("");
                oppDescArea.setText("");
                oppDeadlineField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JTable table = new JTable(reportModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh Reports");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(refreshBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadReports());
        loadReports();

        return panel;
    }

    /** Builds the "Top Skills on Campus" + "Most Active Student" + "Project Popularity" reports. */
    private void loadReports() {
        reportModel.setRowCount(0);
        StudentDAO studentDAO = new StudentDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            reportModel.addRow(new Object[]{"--- Top Skills on Campus ---", ""});
            Map<String, Integer> skills = studentDAO.getTopSkills();
            skills.entrySet().stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(entry -> reportModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

            reportModel.addRow(new Object[]{"--- Most Active Students ---", ""});
            List<Object[]> leaderboard = studentDAO.getMostActiveStudents();
            for (Object[] row : leaderboard) {
                String name = (String) row[0];
                int posted = (Integer) row[1];
                int applied = (Integer) row[2];
                reportModel.addRow(new Object[]{name, posted + " projects, " + applied + " applications"});
            }

            reportModel.addRow(new Object[]{"--- Project Popularity Ranking ---", ""});
            List<Project> popular = projectDAO.getPopularityRanking();
            for (Project p : popular) {
                reportModel.addRow(new Object[]{p.getTitle(), p.getApplicantCount() + " applicants"});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
