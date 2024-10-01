package dao;

import entity.class_user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class ClassUserDAO {

    //get all class_user
    public List<class_user> getAllClassUser() throws SQLException, ClassNotFoundException {
        List<class_user> class_users = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_user";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                class_user cu = new class_user();
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
                class_users.add(cu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return class_users;
    }

    //get class_user by class_id
    public class_user getClassUserByClassId(int class_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_user WHERE class_id = ?";
        class_user cu = new class_user();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cu;
    }

    //get class_user by user_id
    public class_user getClassUserByUserId(int user_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_user WHERE user_id = ?";
        class_user cu = new class_user();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cu;
    }

    //get class_user by user_id and class_id
    public class_user getClassUserByUserIdAndClassId(int user_id, int class_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_user WHERE user_id = ? AND class_id = ?";
        class_user cu = new class_user();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cu;
    }

    //insert class_user
    public boolean insertClassUser(class_user cu) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO class_user(class_id, user_id) VALUES(?, ?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, cu.getClass_id());
            ps.setInt(2, cu.getUser_id());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_user
    public boolean deleteClassUser(int class_id, int user_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_user WHERE class_id = ? AND user_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            ps.setInt(2, user_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_user by class_id
    public boolean deleteClassUserByClassId(int class_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_user WHERE class_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_user by user_id
    public boolean deleteClassUserByUserId(int user_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_user WHERE user_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update class_user
    public boolean updateClassUser(class_user cu) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE class_user SET class_id = ? WHERE user_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, cu.getClass_id());
            ps.setInt(2, cu.getUser_id());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}