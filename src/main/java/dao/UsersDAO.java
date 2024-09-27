package dao;

import model.Users;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsersDAO {

    //get all Users
    public List<Users> getAllUsers() throws SQLException, ClassNotFoundException {
        List<Users> users = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM users";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Users user = new Users();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setAvatar(rs.getBytes("avatar"));
                user.setFeatureFace(rs.getBytes("feature_face"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //get all users by id
    public Users getUsersById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        Users user = new Users();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setAvatar(rs.getBytes("avatar"));
                user.setFeatureFace(rs.getBytes("feature_face"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    //add new user
    public void addUser(Users user) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO users(username, password, email, role_id, created_at, avatar_url, feature_face_url) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRoleId());
            ps.setString(5, user.getCreatedAt());
            ps.setBytes(6, user.getAvatar());
            ps.setBytes(7, user.getFeatureFace());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //update user
    public boolean updateUser(Users user) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role_id = ?, created_at = ?, avatar_url = ?, feature_face_url = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRoleId());
            ps.setString(5, user.getCreatedAt());
            ps.setBytes(6, user.getAvatar());
            ps.setBytes(7, user.getFeatureFace());
            ps.setInt(8, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    //delete user
    public void deleteUser(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  Users getUserByid(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        Users user = new Users();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setAvatar(rs.getBytes("avatar"));
                user.setFeatureFace(rs.getBytes("feature_face"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Users getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM users WHERE username = ?";
        Users user = new Users();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setAvatar(rs.getBytes("avatar"));
                user.setFeatureFace(rs.getBytes("feature_face"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Users getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM users WHERE email = ?";
        Users user = new Users();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getString("created_at"));
                user.setAvatar(rs.getBytes("avatar"));
                user.setFeatureFace(rs.getBytes("feature_face"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UsersDAO usersDAO = new UsersDAO();
        List<Users> users = usersDAO.getAllUsers();
        for (Users user : users) {
            System.out.println(user);
        }
    }

}
