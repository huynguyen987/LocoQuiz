package dao;

import entity.Users;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.*;

public class UsersDAO {

    // Utility method to extract user data from ResultSet
    private Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // Nên mã hóa mật khẩu trước khi hiển thị hoặc sử dụng
        user.setEmail(rs.getString("email"));
        user.setRole_id(rs.getInt("role_id"));
        user.setCreated_at(rs.getString("created_at"));
        user.setAvatar(rs.getBytes("avatar"));
        user.setFeature_face(rs.getBytes("feature_face"));
        user.setGender(rs.getString("gender"));
        return user;
    }

    // Get all Users
    public List<Users> getAllUsers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users";
        List<Users> users = new ArrayList<>();

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users user = extractUserFromResultSet(rs);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể throw lại hoặc xử lý theo cách khác tùy vào yêu cầu
        }

        return users;
    }

    // Get user by ID
    public Users getUserById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Get user by Email
    public Users getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Get user by Username
    public Users getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Add new user
    public boolean addUser(Users user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users(username, password, email, role_id, created_at, avatar, feature_face, gender) VALUES(?,?,?,?,?,?,?,?)";
        boolean isAdded = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRole_id());
            ps.setString(5, user.getCreated_at());
            ps.setBytes(6, user.getAvatar());
            ps.setBytes(7, user.getFeature_face());
            ps.setString(8, user.getGender());

            int affectedRows = ps.executeUpdate();
            isAdded = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // Update user
    public boolean updateUser(Users user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role_id = ?, avatar = ?, feature_face = ?, gender = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRole_id());
            ps.setBytes(5, user.getAvatar());
            ps.setBytes(6, user.getFeature_face());
            ps.setString(7, user.getGender());
            ps.setInt(8, user.getId());

            int affectedRows = ps.executeUpdate();
            isUpdated = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // Delete user
    public boolean deleteUser(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM users WHERE id = ?";
        boolean isDeleted = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            isDeleted = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }// Get all students not in class
    public List<Users> getAllStudentsNotInClass(int classId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE role_id = 2 AND id NOT IN (SELECT user_id FROM class_user WHERE class_id = ?)";
        List<Users> students = new ArrayList<>();
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

    //check login
    public Users checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    // Hash the entered password for comparison
                    String hashedPassword = HashPassword.hashPassword(password);

                    // Compare hashed passwords
                    if (storedHashedPassword.equals(hashedPassword)) {
                        // Authentication successful
                        user = new Users();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setRole_id(rs.getInt("role_id"));
                    }
                }
            }
        }

        return user; // Returns null if authentication fails
    }

    public boolean updateUserForUploadImage(Users user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, email = ?, gender = ?";

        // Kiểm tra nếu avatar không null, thêm vào câu lệnh SQL
        if (user.getAvatar() != null) {
            sql += ", avatar = ?";
        }

        sql += " WHERE id = ?";

        boolean isUpdated = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            ps.setString(paramIndex++, user.getUsername());
            ps.setString(paramIndex++, user.getEmail());
            ps.setString(paramIndex++, user.getGender());

            if (user.getAvatar() != null) {
                ps.setBytes(paramIndex++, user.getAvatar());
            }

            ps.setInt(paramIndex, user.getId());

            int affectedRows = ps.executeUpdate();
            isUpdated = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để Servlet có thể xử lý
        }

        return isUpdated;
    }
}

