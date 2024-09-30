package dao;

import entity.role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;


public class RoleDAO {

    //get all role
    public List<role> getAllRole() throws SQLException, ClassNotFoundException {
        List<role> roles = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM role";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                role r = new role();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                roles.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    //get role by id
    public role getRoleById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM role WHERE id = ?";
        role r = new role();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    //insert role
    public boolean insertRole(role r) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO role(name) VALUES(?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, r.getName());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update role
    public boolean updateRole(role r) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE role SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, r.getName());
            ps.setInt(2, r.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete role
    public boolean deleteRole(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM role WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //get role by name
    public role getRoleByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM role WHERE name = ?";
        role r = new role();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    //get role by name
    public role getRoleByUserId(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM role WHERE id = (SELECT role_id FROM user WHERE id = ?)";
        role r = new role();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    //get role by name
    public role getRoleByUserName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM role WHERE id = (SELECT role_id FROM user WHERE username = ?)";
        role r = new role();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

  //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RoleDAO dao = new RoleDAO();
        List<role> list = dao.getAllRole();
        for (role r : list) {
            System.out.println(r.getId() + " - " + r.getName());
        }
    }


}
