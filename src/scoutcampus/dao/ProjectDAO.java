package scoutcampus.dao;

import scoutcampus.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public boolean createProject(Project p) throws SQLException {
        String sql = "INSERT INTO projects (title, description, required_skills, team_size, owner_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitle());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getRequiredSkills());
            ps.setInt(4, p.getTeamSize());
            ps.setInt(5, p.getOwnerId());
            ps.setString(6, p.getStatus() == null ? "Open" : p.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Project> getAllProjects() throws SQLException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name AS owner_name, " +
                "(SELECT COUNT(*) FROM applications a WHERE a.project_id = p.project_id) AS applicant_count " +
                "FROM projects p JOIN students s ON p.owner_id = s.student_id " +
                "ORDER BY p.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Skill-based project search: matches projects whose required_skills contains the keyword. */
    public List<Project> searchBySkill(String keyword) throws SQLException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name AS owner_name, " +
                "(SELECT COUNT(*) FROM applications a WHERE a.project_id = p.project_id) AS applicant_count " +
                "FROM projects p JOIN students s ON p.owner_id = s.student_id " +
                "WHERE p.required_skills LIKE ? OR p.title LIKE ? " +
                "ORDER BY p.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Project> getByOwner(int ownerId) throws SQLException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name AS owner_name, " +
                "(SELECT COUNT(*) FROM applications a WHERE a.project_id = p.project_id) AS applicant_count " +
                "FROM projects p JOIN students s ON p.owner_id = s.student_id " +
                "WHERE p.owner_id = ? ORDER BY p.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** Project popularity ranking, most applied-to first. */
    public List<Project> getPopularityRanking() throws SQLException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT p.*, s.name AS owner_name, " +
                "(SELECT COUNT(*) FROM applications a WHERE a.project_id = p.project_id) AS applicant_count " +
                "FROM projects p JOIN students s ON p.owner_id = s.student_id " +
                "ORDER BY applicant_count DESC LIMIT 10";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean updateStatus(int projectId, String status) throws SQLException {
        String sql = "UPDATE projects SET status = ? WHERE project_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, projectId);
            return ps.executeUpdate() > 0;
        }
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        Project p = new Project(rs.getInt("project_id"), rs.getString("title"),
                rs.getString("description"), rs.getString("required_skills"),
                rs.getInt("team_size"), rs.getInt("owner_id"), rs.getString("status"));
        p.setOwnerName(rs.getString("owner_name"));
        p.setApplicantCount(rs.getInt("applicant_count"));
        return p;
    }
}
