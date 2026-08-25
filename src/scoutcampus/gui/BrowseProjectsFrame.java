package scoutcampus.gui;

import scoutcampus.dao.ApplicationDAO;
import scoutcampus.dao.ProjectDAO;
import scoutcampus.model.Project;
import scoutcampus.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BrowseProjectsFrame extends JFrame {

    private final Student student;
    private final JTextField searchField = new JTextField(20);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Skills Needed", "Team Size", "Owner", "Status", "Applicants"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public BrowseProjectsFrame(Student student) {
        this.student = student;

        setTitle("Browse Projects");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 460);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by skill or title:"));
        searchPanel.add(searchField);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Show All");
        JButton popularBtn = new JButton("Sort by Popularity");
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);
        searchPanel.add(popularBtn);
        main.add(searchPanel, BorderLayout.NORTH);

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton applyBtn = new JButton("Apply to Selected Project");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(applyBtn);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);

        searchBtn.addActionListener(e -> loadProjects(searchField.getText().trim()));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            loadProjects(null);
        });
        popularBtn.addActionListener(e -> loadPopularityRanking());
        applyBtn.addActionListener(e -> applyToSelected());

        loadProjects(null);
    }

    private void loadProjects(String keyword) {
        tableModel.setRowCount(0);
        ProjectDAO dao = new ProjectDAO();
        try {
            List<Project> projects = (keyword == null || keyword.isEmpty())
                    ? dao.getAllProjects() : dao.searchBySkill(keyword);
            for (Project p : projects) {
                tableModel.addRow(new Object[]{p.getProjectId(), p.getTitle(), p.getRequiredSkills(),
                        p.getTeamSize(), p.getOwnerName(), p.getStatus(), p.getApplicantCount()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPopularityRanking() {
        tableModel.setRowCount(0);
        ProjectDAO dao = new ProjectDAO();
        try {
            List<Project> projects = dao.getPopularityRanking();
            for (Project p : projects) {
                tableModel.addRow(new Object[]{p.getProjectId(), p.getTitle(), p.getRequiredSkills(),
                        p.getTeamSize(), p.getOwnerName(), p.getStatus(), p.getApplicantCount()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyToSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a project first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int projectId = (Integer) tableModel.getValueAt(row, 0);

        // POLYMORPHISM: calling apply() through the Applicant interface reference
        scoutcampus.interfaces.Applicant applicant = student;
        applicant.apply();

        ApplicationDAO dao = new ApplicationDAO();
        try {
            boolean success = dao.apply(projectId, student.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Application submitted!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProjects(null);
            }
        } catch (SQLException ex) {
            // e.g. duplicate application triggers the UNIQUE constraint
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
