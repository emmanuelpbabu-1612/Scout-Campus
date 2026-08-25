package scoutcampus.gui;

import scoutcampus.dao.ApplicationDAO;
import scoutcampus.model.Application;
import scoutcampus.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ApplicationsFrame extends JFrame {

    private final Student student;
    private final ApplicationDAO dao = new ApplicationDAO();

    private final DefaultTableModel sentModel = new DefaultTableModel(
            new String[]{"App ID", "Project", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel receivedModel = new DefaultTableModel(
            new String[]{"App ID", "Project", "Applicant", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable receivedTable = new JTable(receivedModel);

    public ApplicationsFrame(Student student) {
        this.student = student;

        setTitle("My Applications");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(680, 460);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        JTable sentTable = new JTable(sentModel);
        tabs.addTab("Applications Sent", new JScrollPane(sentTable));

        JPanel receivedPanel = new JPanel(new BorderLayout());
        receivedPanel.add(new JScrollPane(receivedTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton acceptBtn = new JButton("Accept");
        JButton rejectBtn = new JButton("Reject");
        actionPanel.add(acceptBtn);
        actionPanel.add(rejectBtn);
        receivedPanel.add(actionPanel, BorderLayout.SOUTH);

        tabs.addTab("Applications Received", receivedPanel);

        add(tabs);

        acceptBtn.addActionListener(e -> updateSelected("Accepted"));
        rejectBtn.addActionListener(e -> updateSelected("Rejected"));

        loadData();
    }

    private void loadData() {
        sentModel.setRowCount(0);
        receivedModel.setRowCount(0);
        try {
            List<Application> sent = dao.getSentByStudent(student.getId());
            for (Application a : sent) {
                sentModel.addRow(new Object[]{a.getApplicationId(), a.getProjectTitle(), a.getStatus()});
            }

            List<Application> received = dao.getReceivedByOwner(student.getId());
            for (Application a : received) {
                receivedModel.addRow(new Object[]{a.getApplicationId(), a.getProjectTitle(),
                        a.getStudentName(), a.getStatus()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSelected(String status) {
        int row = receivedTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int applicationId = (Integer) receivedModel.getValueAt(row, 0);
        try {
            dao.updateStatus(applicationId, status);
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
