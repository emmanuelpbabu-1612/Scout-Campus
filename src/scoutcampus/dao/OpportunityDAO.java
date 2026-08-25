package scoutcampus.dao;

import scoutcampus.model.Opportunity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OpportunityDAO {

    public boolean create(Opportunity o) throws SQLException {
        String sql = "INSERT INTO opportunities (title, type, description, deadline) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, o.getTitle());
            ps.setString(2, o.getType());
            ps.setString(3, o.getDescription());
            ps.setDate(4, o.getDeadline());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Opportunity> getAll() throws SQLException {
        List<Opportunity> list = new ArrayList<>();
        String sql = "SELECT * FROM opportunities ORDER BY deadline ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Opportunity(rs.getInt("opportunity_id"), rs.getString("title"),
                        rs.getString("type"), rs.getString("description"), rs.getDate("deadline")));
            }
        }
        return list;
    }

    public boolean save(int studentId, int opportunityId) throws SQLException {
        String sql = "INSERT IGNORE INTO saved_opportunities (student_id, opportunity_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, opportunityId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Opportunity> getSavedByStudent(int studentId) throws SQLException {
        List<Opportunity> list = new ArrayList<>();
        String sql = "SELECT o.* FROM opportunities o " +
                "JOIN saved_opportunities so ON o.opportunity_id = so.opportunity_id " +
                "WHERE so.student_id = ? ORDER BY o.deadline ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Opportunity(rs.getInt("opportunity_id"), rs.getString("title"),
                            rs.getString("type"), rs.getString("description"), rs.getDate("deadline")));
                }
            }
        }
        return list;
    }
}
