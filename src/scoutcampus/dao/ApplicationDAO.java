package scoutcampus.dao;

import scoutcampus.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    /** Student applies to a project. Wrapped in try/catch by the caller for EXCEPTION HANDLING. */
    public boolean apply(int projectId, int studentId) throws SQLException {
        String sql = "INSERT INTO applications (project_id, student_id, status) VALUES (?, ?, 'Pending')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Applications a given student has sent out. */
    public List<Application> getSentByStudent(int studentId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, p.title AS project_title FROM applications a " +
                "JOIN projects p ON a.project_id = p.project_id " +
                "WHERE a.student_id = ? ORDER BY a.applied_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** Applications received on projects a given student owns. */
    public List<Application> getReceivedByOwner(int ownerId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, p.title AS project_title, s.name AS student_name " +
                "FROM applications a " +
                "JOIN projects p ON a.project_id = p.project_id " +
                "JOIN students s ON a.student_id = s.student_id " +
                "WHERE p.owner_id = ? ORDER BY a.applied_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application a = mapRow(rs);
                    a.setStudentName(rs.getString("student_name"));
                    list.add(a);
                }
            }
        }
        return list;
    }

    public boolean updateStatus(int applicationId, String status) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countByStudentAndStatus(int studentId, String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM applications WHERE student_id = ? AND status = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    private Application mapRow(ResultSet rs) throws SQLException {
        Application a = new Application(rs.getInt("application_id"), rs.getInt("project_id"),
                rs.getInt("student_id"), rs.getString("status"));
        a.setProjectTitle(rs.getString("project_title"));
        return a;
    }
}
