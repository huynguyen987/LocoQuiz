package dao;

import model.ClassUsers;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ClassUserDAO {

    //get all classUser id
    public static ArrayList<ClassUsers> getAllClassUsers() {
        ArrayList<ClassUsers> classUsers = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM class_users");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                ClassUsers cu = new ClassUsers();
                cu.setUser_id(rs.getInt("user_id"));
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
                classUsers.add(cu);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classUsers;
    }

    //get classUser by user_id
    public static ClassUsers getClassUserByUserId(int user_id) {
        ClassUsers cu = new ClassUsers();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM class_users WHERE user_id = ?");
            pstm.setInt(1, user_id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                cu.setUser_id(rs.getInt("user_id"));
                cu.setClass_id(rs.getInt("class_id"));
                cu.setUser_id(rs.getInt("user_id"));
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cu;
    }

    //add classUser
    public static void addClassUser(ClassUsers cu) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO class_users(user_id, class_id) VALUES(?, ?)");
            pstm.setInt(1, cu.getUser_id());
            pstm.setInt(2, cu.getClass_id());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //update classUser
    public static void updateClassUser(ClassUsers cu) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("UPDATE class_users SET user_id = ?, class_id = ? WHERE user_id = ?");
            pstm.setInt(1, cu.getUser_id());
            pstm.setInt(2, cu.getClass_id());
            pstm.setInt(3, cu.getUser_id());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //delete classUser
    public static void deleteClassUser(int user_id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM class_users WHERE user_id = ?");
            pstm.setInt(1, user_id);
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //main method
    public static void main(String[] args) {
        //test getAllClassUsers
        ArrayList<ClassUsers> classUsers = getAllClassUsers();
        for (ClassUsers cu : classUsers) {
            System.out.println(cu);
        }
    }

}
