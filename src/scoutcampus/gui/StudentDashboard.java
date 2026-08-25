package scoutcampus.gui;

import scoutcampus.dao.ApplicationDAO;
import scoutcampus.dao.OpportunityDAO;
import scoutcampus.dao.ProjectDAO;
import scoutcampus.model.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StudentDashboard extends JFrame {

    private final Student student;
    private final JLabel projectsPostedLabel = new JLabel("--");
    private final JLabel applicationsSentLabel = new JLabel("--");
    private final JLabel applicationsReceivedLabel = new JLabel("--");
    private final JLabel upcomingEventsLabel = new JLabel("--");

    public StudentDashboard(Student student) {
        this.student = student;

        setTitle("Scout Campus - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 420);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcome = new JLabel("Welcome, " + student.getName() + " \uD83D\uDC4B");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 20));
        main.add(welcome, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Your Stats"));
        statsPanel.add(statCard("Projects Posted", projectsPostedLabel));
        statsPanel.add(statCard("Applications Sent", applicationsSentLabel));
        statsPanel.add(statCard("Applications Received", applicationsReceivedLabel));
        statsPanel.add(statCard("Upcoming Events", upcomingEventsLabel));
        main.add(statsPanel, BorderLayout.CENTER);

        JPanel navPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        JButton profileBtn = new JButton("My Profile");
        JButton createProjectBtn = new JButton("Post a Project");
        JButton browseProjectsBtn = new JButton("Browse Projects");
        JButton applicationsBtn = new JButton("My Applications");
        JButton opportunitiesBtn = new JButton("Opportunities");
        JButton logoutBtn = new JButton("Logout");

        navPanel.add(profileBtn);
        navPanel.add(createProjectBtn);
        navPanel.add(browseProjectsBtn);
        navPanel.add(applicationsBtn);
        navPanel.add(opportunitiesBtn);
        navPanel.add(logoutBtn);

        main.add(navPanel, BorderLayout.SOUTH);
        add(main);

        profileBtn.addActionListener(e -> new ProfileFrame(student).setVisible(true));
        createProjectBtn.addActionListener(e -> new CreateProjectFrame(student, this).setVisible(true));
        browseProjectsBtn.addActionListener(e -> new BrowseProjectsFrame(student).setVisible(true));
        applicationsBtn.addActionListener(e -> new ApplicationsFrame(student).setVisible(true));
        opportunitiesBtn.addActionListener(e -> new OpportunityBoardFrame(student).setVisible(true));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        refreshStats();
    }

    private JPanel statCard(String label, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel captionLabel = new JLabel(label, SwingConstants.CENTER);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(captionLabel, BorderLayout.SOUTH);
        return card;
    }

    /** Refreshes the dashboard counts from the database. Public so it can be called after posting a project. */
    public void refreshStats() {
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            ApplicationDAO applicationDAO = new ApplicationDAO();
            OpportunityDAO opportunityDAO = new OpportunityDAO();

            int posted = projectDAO.getByOwner(student.getId()).size();
            int sent = applicationDAO.getSentByStudent(student.getId()).size();
            int received = applicationDAO.getReceivedByOwner(student.getId()).size();
            int events = opportunityDAO.getAll().size();

            projectsPostedLabel.setText(String.valueOf(posted));
            applicationsSentLabel.setText(String.valueOf(sent));
            applicationsReceivedLabel.setText(String.valueOf(received));
            upcomingEventsLabel.setText(String.valueOf(events));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
