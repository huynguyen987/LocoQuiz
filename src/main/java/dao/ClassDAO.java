package dao;

import entity.classs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class ClassDAO {

    //createClass
    public boolean createClass(classs newClass) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Connect to the database
            connection = new DBConnect().getConnection();

            // SQL query to insert a new class
            String sql = "INSERT INTO class (name, class_key, description, teacher_id) VALUES (?, ?, ?, ?)";

            // Prepare the statement and specify that you want to retrieve generated keys
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newClass.getName());
            statement.setString(2, newClass.getClass_key());
            statement.setString(3, newClass.getDescription());
            statement.setInt(4, newClass.getTeacher_id());

            // Execute the update
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Retrieve the generated key
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    newClass.setId(id); // Set the ID in the newClass object
                }
                return true;
            } else {
                return false;
            }
        } finally {
            // Close resources
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }


    //get all class
    public List<classs> getAllClass() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM class";
        List<classs> classs = new ArrayList<>();
        try {
            Connection connection = new DBConnect().getConnection();
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

    public classs getClassById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM class WHERE id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    classs classEntity = new classs();
                    classEntity.setId(rs.getInt("id"));
                    classEntity.setName(rs.getString("name"));
                    classEntity.setClass_key(rs.getString("class_key"));
                    classEntity.setDescription(rs.getString("description"));
                    classEntity.setCreated_at(rs.getString("created_at"));
                    classEntity.setUpdated_at(rs.getString("updated_at"));
                    classEntity.setTeacher_id(rs.getInt("teacher_id"));
                    return classEntity;
                }
            }
        }
        return null;
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
    public boolean updateClass(classs classEntity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE class SET name = ?, description = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, classEntity.getName());
            ps.setString(2, classEntity.getDescription());
            ps.setInt(3, classEntity.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteClass(int classId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM class WHERE id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
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

    public List<classs> searchClasses(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM class WHERE name LIKE ?";
        List<classs> classes = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    classs classEntity = new classs();
                    classEntity.setId(rs.getInt("id"));
                    classEntity.setName(rs.getString("name"));
                    classEntity.setClass_key(rs.getString("class_key"));
                    classEntity.setDescription(rs.getString("description"));
                    classEntity.setCreated_at(rs.getString("created_at"));
                    classEntity.setUpdated_at(rs.getString("updated_at"));
                    classEntity.setTeacher_id(rs.getInt("teacher_id"));
                    classes.add(classEntity);
                }
            }
        }
        return classes;
    }

}
