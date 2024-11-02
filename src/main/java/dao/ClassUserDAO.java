package dao;

import entity.JoinRequest;
import entity.Users;
import entity.class_user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;
import entity.classs;

public class ClassUserDAO {

    //isStudentEnrolled
    public boolean isStudentEnrolled(int classId, int studentId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM class_user WHERE class_id = ? AND user_id = ? AND status = 'approved'";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    //getStudentsByClassId
    public List<Users> getStudentsByClassId(int classId) throws SQLException, ClassNotFoundException {
        List<Users> students = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN class_user cu ON u.id = cu.user_id WHERE cu.class_id = ? AND cu.status = 'approved'";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users student = new Users();
                    student.setId(rs.getInt("id"));
                    student.setUsername(rs.getString("username"));
                    student.setEmail(rs.getString("email"));
                    students.add(student);
                }
            }
        }
        return students;
    }


    public List<Users> getStudentsByClassIdAll(int classId) throws SQLException, ClassNotFoundException {
        List<Users> students = new ArrayList<>();
        String sql = "SELECT u.*, cu.status FROM users u " +
                "JOIN class_user cu ON u.id = cu.user_id " +
                "WHERE cu.class_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users student = new Users();
                    student.setId(rs.getInt("id"));
                    student.setUsername(rs.getString("username"));
                    student.setEmail(rs.getString("email"));
                    // Thêm thuộc tính trạng thái nếu cần
                    String status = rs.getString("status");
                    // Bạn có thể sử dụng status để hiển thị trong JSP
                    students.add(student);
                }
            }
        }
        return students;
    }


    // Get users by class ID (approved only)
    public List<Users> getUsersByClassId(int classId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT u.* FROM users u JOIN class_user cu ON u.id = cu.user_id WHERE cu.class_id = ? AND cu.status = 'approved'";
        List<Users> users = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    users.add(user);
                }
            }
        }
        return users;
    }

    // Enroll student to class (approved status)
    public boolean enrollStudentToClass(int classId, int studentId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO class_user(class_id, user_id, status) VALUES(?, ?, 'approved')";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            return ps.executeUpdate() == 1;
        }
    }

    // Submit a join request
    public boolean submitJoinRequest(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO class_user (class_id, user_id, status) VALUES (?, ?, 'pending')";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle duplicate requests gracefully
            return false;
        }
    }

    // Accept a join request
    public boolean acceptJoinRequest(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE class_user SET status = 'approved', responded_at = CURRENT_TIMESTAMP WHERE class_id = ? AND user_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Reject a join request
    public boolean rejectJoinRequest(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE class_user SET status = 'rejected', responded_at = CURRENT_TIMESTAMP WHERE class_id = ? AND user_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Remove a student from a class
    public boolean removeStudentFromClass(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM class_user WHERE class_id = ? AND user_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // Get all pending join requests for a class
    public List<JoinRequest> getPendingJoinRequests(int classId) throws SQLException, ClassNotFoundException {
        List<JoinRequest> requests = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, cu.requested_at " +
                "FROM class_user cu " +
                "JOIN users u ON cu.user_id = u.id " +
                "WHERE cu.class_id = ? AND cu.status = 'pending'";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JoinRequest jr = new JoinRequest();
                    jr.setUserId(rs.getInt("id"));
                    jr.setUsername(rs.getString("username"));
                    jr.setEmail(rs.getString("email"));
                    jr.setRequestedAt(rs.getTimestamp("requested_at"));
                    jr.setClassId(classId);
                    requests.add(jr);
                }
            }
        }
        return requests;
    }

    // Check if a join request already exists and is pending or approved
    public boolean hasPendingOrApprovedRequest(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM class_user WHERE class_id = ? AND user_id = ? AND status IN ('pending', 'approved')";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    //isUserEnrolledInClass
    public boolean isUserEnrolledInClass(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM class_user WHERE class_id = ? AND user_id = ? AND status = 'approved'";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    //sendJoinRequest
    public boolean sendJoinRequest(int classId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO class_user (class_id, user_id, status) VALUES (?, ?, 'pending')";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLIntegrityConstraintViolationException e) {

            return false;
        }
    }
}
