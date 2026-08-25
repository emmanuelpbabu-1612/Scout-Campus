package scoutcampus.dao;

import scoutcampus.model.Admin;
import scoutcampus.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {

    /** Registers a new student. Returns true on success. */
    public boolean register(Student s) throws SQLException {
        String sql = "INSERT INTO students (name, email, department, year, password) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getDepartment());
            ps.setString(4, s.getYear());
            ps.setString(5, s.getPassword());
            return ps.executeUpdate() > 0;
        }
    }

    /** Attempts a student login. Returns the Student on success, or null. */
    public Student login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM students WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /** Admin login is intentionally kept separate (hard-coded admins table). */
    public Admin loginAdmin(String email, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("admin_id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    public Student getById(int studentId) throws SQLException {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public boolean updateProfile(Student s) throws SQLException {
        String sql = "UPDATE students SET bio=?, skills=?, interests=?, github=?, " +
                "linkedin=?, startup_interests=? WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getBio());
            ps.setString(2, s.getSkills());
            ps.setString(3, s.getInterests());
            ps.setString(4, s.getGithub());
            ps.setString(5, s.getLinkedin());
            ps.setString(6, s.getStartupInterests());
            ps.setInt(7, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /** "Top Skills on Campus" report: counts how often each skill token appears. */
    public Map<String, Integer> getTopSkills() throws SQLException {
        Map<String, Integer> counts = new HashMap<>();
        String sql = "SELECT skills FROM students WHERE skills IS NOT NULL AND skills <> ''";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String raw = rs.getString("skills");
                for (String skill : raw.split(",")) {
                    String trimmed = skill.trim();
                    if (!trimmed.isEmpty()) {
                        counts.merge(trimmed, 1, Integer::sum);
                    }
                }
            }
        }
        return counts;
    }

    /** "Most Active Student" leaderboard: projects posted + applications sent. */
    public List<Object[]> getMostActiveStudents() throws SQLException {
        List<Object[]> leaderboard = new ArrayList<>();
        String sql =
                "SELECT s.name, " +
                "  (SELECT COUNT(*) FROM projects p WHERE p.owner_id = s.student_id) AS posted, " +
                "  (SELECT COUNT(*) FROM applications a WHERE a.student_id = s.student_id) AS applied " +
                "FROM students s " +
                "ORDER BY (posted + applied) DESC LIMIT 10";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                leaderboard.add(new Object[]{
                        rs.getString("name"),
                        rs.getInt("posted"),
                        rs.getInt("applied")
                });
            }
        }
        return leaderboard;
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student(rs.getInt("student_id"), rs.getString("name"),
                rs.getString("email"), rs.getString("password"),
                rs.getString("department"), rs.getString("year"));
        s.setBio(rs.getString("bio"));
        s.setSkills(rs.getString("skills"));
        s.setInterests(rs.getString("interests"));
        s.setGithub(rs.getString("github"));
        s.setLinkedin(rs.getString("linkedin"));
        s.setStartupInterests(rs.getString("startup_interests"));
        return s;
    }
}
