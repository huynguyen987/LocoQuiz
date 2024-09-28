package model;

import Module.DBConnect;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public UserDAO() {
    }

    public boolean addUser(User user) {
        int n = 0;
        String sql = "INSERT INTO Users(username, email, password_hash, full_name, gender, role_id, status) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, user.getUsername());
            pre.setString(2, user.getEmail());
            pre.setString(3, user.getPasswordHash());
            pre.setString(4, user.getFullName());
            pre.setString(5, user.getGender()); // Assuming User class has getGender()
            pre.setInt(6, user.getRoleId());    // Assuming User class has getRoleId()
            pre.setString(7, user.getStatus()); // Assuming User class has getStatus()
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return n > 0;
    }

    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setGender(rs.getString("gender")); // Assuming User class has setGender()
                    user.setRoleId(rs.getInt("role_id"));   // Assuming User class has setRoleId()
                    user.setStatus(rs.getString("status")); // Assuming User class has setStatus()
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public boolean updateUser(User user) {
        int n = 0;
        String sql = "UPDATE Users SET username = ?, email = ?, password_hash = ?, full_name = ?, gender = ?, role_id = ?, status = ? WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, user.getUsername());
            pre.setString(2, user.getEmail());
            pre.setString(3, user.getPasswordHash());
            pre.setString(4, user.getFullName());
            pre.setString(5, user.getGender()); // Assuming User class has getGender()
            pre.setInt(6, user.getRoleId());    // Assuming User class has getRoleId()
            pre.setString(7, user.getStatus()); // Assuming User class has getStatus()
            pre.setInt(8, user.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return n > 0;
    }

    public boolean removeUser(int id) {
        int n = 0;
        String sql = "DELETE FROM Users WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return n > 0;
    }

    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setFullName(rs.getString("full_name"));
                user.setGender(rs.getString("gender")); // Assuming User class has setGender()
                user.setRoleId(rs.getInt("role_id"));   // Assuming User class has setRoleId()
                user.setStatus(rs.getString("status")); // Assuming User class has setStatus()
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        List<User> list = dao.getAllUser();

        for (User user : list) {
            System.out.println(user.getId() + " " +
                    user.getUsername() + " " +
                    user.getEmail() + " " +
                    user.getPasswordHash() +
                    " " + user.getFullName());
        }

        User user = new User();
        user.setUsername("user1");
        user.setEmail("user@gmail.com");
        user.setPasswordHash("123456");
        user.setFullName("User 1");
        user.setGender("Male"); // Added gender
        user.setRoleId(1);      // Added role ID
        user.setStatus("active"); // Added status
        dao.addUser(user);
    }

    public boolean updatePassword(User user) {
        int n = 0;
        String sql = "UPDATE Users SET password_hash = ? WHERE email = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, user.getPasswordHash());
            pre.setString(2, user.getEmail());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE email = ?";
        // code trả về một user có email là email
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, email);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setGender(rs.getString("gender"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = null;
        String sql = "SELECT * FROM Users WHERE username = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, username);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    user.setGender(rs.getString("gender"));
                    user.setRoleId(rs.getInt("role_id"));
                    user.setStatus(rs.getString("status"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    user.setAvatar(rs.getBytes("avatar")); // If you have an avatar column
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

}
