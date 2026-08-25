package scoutcampus.gui;

import scoutcampus.dao.OpportunityDAO;
import scoutcampus.model.Opportunity;
import scoutcampus.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OpportunityBoardFrame extends JFrame {

    private final Student student;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Title", "Type", "Deadline"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public OpportunityBoardFrame(Student student) {
        this.student = student;

        setTitle("Opportunity Board");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Hackathons, Competitions, Workshops & Startup Events", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        main.add(header, BorderLayout.NORTH);

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save Opportunity");
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(saveBtn);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);

        saveBtn.addActionListener(e -> saveSelected());

        loadOpportunities();
    }

    private void loadOpportunities() {
        model.setRowCount(0);
        OpportunityDAO dao = new OpportunityDAO();
        try {
            List<Opportunity> list = dao.getAll();
            for (Opportunity o : list) {
                model.addRow(new Object[]{o.getOpportunityId(), o.getTitle(), o.getType(), o.getDeadline()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an opportunity first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int opportunityId = (Integer) model.getValueAt(row, 0);
        OpportunityDAO dao = new OpportunityDAO();
        try {
            dao.save(student.getId(), opportunityId);
            JOptionPane.showMessageDialog(this, "Opportunity saved to your dashboard.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
