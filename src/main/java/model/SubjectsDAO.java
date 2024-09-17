package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.Subjects;
import Module.DBConnect;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubjectsDAO {
    // Add a subject
    public boolean addSubject(Subjects subject) {
        int n = 0;
        String sql = "INSERT INTO Subjects(title, description, category, thumbnail_url, status, created_by) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, subject.getTitle());
            pre.setString(2, subject.getDescription());
            pre.setString(3, subject.getCategory());
            pre.setString(4, subject.getThumbnailUrl());
            pre.setString(5, subject.getStatus());
            pre.setInt(6, subject.getCreatedBy());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Get all subjects
    public List<Subjects> getAllSubjects() {
        List<Subjects> subjects = new ArrayList<>();
        String sql = "SELECT * FROM Subjects";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                Subjects subject = new Subjects();
                subject.setId(rs.getInt("id"));
                subject.setTitle(rs.getString("title"));
                subject.setDescription(rs.getString("description"));
                subject.setCategory(rs.getString("category"));
                subject.setThumbnailUrl(rs.getString("thumbnail_url"));
                subject.setStatus(rs.getString("status"));
                subject.setCreatedBy(rs.getInt("created_by"));
                subject.setCreatedAt(rs.getTimestamp("created_at"));
                subject.setUpdatedAt(rs.getTimestamp("updated_at"));
                subject.setAvatar(rs.getBytes("avatar")); // Assuming the avatar is stored as bytes
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    // Get subject by ID
    public Subjects getSubjectById(int id) {
        Subjects subject = null;
        String sql = "SELECT * FROM Subjects WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    subject = new Subjects();
                    subject.setId(rs.getInt("id"));
                    subject.setTitle(rs.getString("title"));
                    subject.setDescription(rs.getString("description"));
                    subject.setCategory(rs.getString("category"));
                    subject.setThumbnailUrl(rs.getString("thumbnail_url"));
                    subject.setStatus(rs.getString("status"));
                    subject.setCreatedBy(rs.getInt("created_by"));
                    subject.setCreatedAt(rs.getTimestamp("created_at"));
                    subject.setUpdatedAt(rs.getTimestamp("updated_at"));
                    subject.setAvatar(rs.getBytes("avatar"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    // Update a subject
    public boolean updateSubject(Subjects subject) {
        int n = 0;
        String sql = "UPDATE Subjects SET title = ?, description = ?, category = ?, thumbnail_url = ?, status = ?, avatar = ? WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, subject.getTitle());
            pre.setString(2, subject.getDescription());
            pre.setString(3, subject.getCategory());
            pre.setString(4, subject.getThumbnailUrl());
            pre.setString(5, subject.getStatus());
            pre.setBytes(6, subject.getAvatar());
            pre.setInt(7, subject.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Remove a subject
    public boolean removeSubject(int id) {
        int n = 0;
        String sql = "DELETE FROM Subjects WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
   //main method for testing
    public static void main(String[] args) {
        SubjectsDAO subjectsDAO = new SubjectsDAO();
        List<Subjects> subjects = subjectsDAO.getAllSubjects();
        for (Subjects subject : subjects) {
            System.out.println(subject.getCreatedAt());
        }
    }
}
