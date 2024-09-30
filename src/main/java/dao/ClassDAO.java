package dao;

import entity.classs;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class ClassDAO {

    //get all class
    public List<classs> getAllClass() throws SQLException, ClassNotFoundException {
        List<classs> classs = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classs c = new classs();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setCreated_at(rs.getTimestamp("created_at").toString());
                c.setUpdated_at(rs.getTimestamp("updated_at").toString());
                c.setTeacher_id(rs.getInt("teacher_id"));
                classs.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classs;
    }

    //get class by id
    public classs getClassById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class WHERE id = ?";
        classs c = new classs();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setCreated_at(rs.getTimestamp("created_at").toString());
                c.setUpdated_at(rs.getTimestamp("updated_at").toString());
                c.setTeacher_id(rs.getInt("teacher_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    //insert class
    public boolean insertClass(classs c) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO class(name, description, teacher_id) VALUES(?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getTeacher_id());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update class
    public boolean updateClass(classs c) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE class SET name = ?, description = ?, teacher_id = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getTeacher_id());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class
    public boolean deleteClass(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //get class by teacher_id
    public List<classs> getClassByTeacherId(int teacher_id) throws SQLException, ClassNotFoundException {
        List<classs> classs = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class WHERE teacher_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, teacher_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                classs c = new classs();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setCreated_at(rs.getTimestamp("created_at").toString());
                c.setUpdated_at(rs.getTimestamp("updated_at").toString());
                c.setTeacher_id(rs.getInt("teacher_id"));
                classs.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classs;
    }

    //get class by name
    public classs getClassByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class WHERE name = ?";
        classs c = new classs();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setCreated_at(rs.getTimestamp("created_at").toString());
                c.setUpdated_at(rs.getTimestamp("updated_at").toString());
                c.setTeacher_id(rs.getInt("teacher_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

}
