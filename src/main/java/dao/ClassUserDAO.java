package dao;

import entity.Users;
import entity.class_user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;
import entity.classs;

public class ClassUserDAO {

    //get user by class_id
    public List<Users> getUsersByClassId(int classId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT u.* FROM users u JOIN class_user cu ON u.id = cu.user_id WHERE cu.class_id = ?";
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

    //enroll student to class
    public boolean enrollStudentToClass(int classId, int studentId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO class_user(class_id, user_id) VALUES(?, ?)";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            return ps.executeUpdate() == 1;
        }
    }

   //isUserEnrolledInClass
    public boolean isUserEnrolledInClass(int classId, int studentId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM class_user WHERE class_id = ? AND user_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    //get class by user_id
    public List<classs> getClassesByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.* FROM classs c JOIN class_user cu ON c.id = cu.class_id WHERE cu.user_id = ?";
        List<classs> classes = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    classs classEntity = new classs();
                    classEntity.setId(rs.getInt("id"));
                    classEntity.setName(rs.getString("name"));
                    classEntity.setClass_key(rs.getString("class_key"));
                    classes.add(classEntity);
                }
            }
        }
        return classes;
    }




}


