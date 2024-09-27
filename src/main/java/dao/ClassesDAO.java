package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Classes;
import Module.*;
import java.util.List;

public class ClassesDAO {
    //get all classes by id
    public List<Classes> getAllClasses() {
        List<Classes> classes = new ArrayList<>();
        String query = "SELECT * FROM classes ORDER BY created_at DESC";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Classes cls = new Classes();
                cls.setId(rs.getInt("id"));
                cls.setName(rs.getString("name"));
                cls.setClass_key(rs.getString("class_key"));
                cls.setDescription(rs.getString("description"));
                cls.setCreated_at(rs.getString("created_at"));
                cls.setUpdated_at(rs.getString("updated_at"));
                cls.setTeacher_id(rs.getInt("teacher_id"));
                classes.add(cls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    //get class by id
    public Classes getClassById(int id) {
        Classes cls = null;
        String query = "SELECT * FROM classes WHERE id = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cls = new Classes();
                    cls.setId(rs.getInt("id"));
                    cls.setName(rs.getString("name"));
                    cls.setClass_key(rs.getString("class_key"));
                    cls.setDescription(rs.getString("description"));
                    cls.setCreated_at(rs.getString("created_at"));
                    cls.setUpdated_at(rs.getString("updated_at"));
                    cls.setTeacher_id(rs.getInt("teacher_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }

    //add classes
    public void addClasses(Classes cls) {
        String query = "INSERT INTO classes(name, class_key, description, created_at, updated_at, teacher_id) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cls.getName());
            stmt.setString(2, cls.getClass_key());
            stmt.setString(3, cls.getDescription());
            stmt.setString(4, cls.getCreated_at());
            stmt.setString(5, cls.getUpdated_at());
            stmt.setInt(6, cls.getTeacher_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //update classes
    public void updateClasses(Classes cls) {
        String query = "UPDATE classes SET name = ?, class_key = ?, description = ?, updated_at = ?, teacher_id = ? WHERE id = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cls.getName());
            stmt.setString(2, cls.getClass_key());
            stmt.setString(3, cls.getDescription());
            stmt.setString(4, cls.getUpdated_at());
            stmt.setInt(5, cls.getTeacher_id());
            stmt.setInt(6, cls.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //delete classes
    public void deleteClasses(int id) {
        String query = "DELETE FROM classes WHERE id = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //main method
    public static void main(String[] args) {
        ClassesDAO dao = new ClassesDAO();
        List<Classes> classes = dao.getAllClasses();
        for (Classes cls : classes) {
            System.out.println(cls);
        }
    }

}
