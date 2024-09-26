package dao;

import model.Roles;
import Module.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RolesDAO {

    //get all roles
    public static ArrayList<Roles> getAllRoles() {
        ArrayList<Roles> roles = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM roles");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Roles r = new Roles();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                roles.add(r);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    //get role by id
    public static Roles getRoleById(int id) {
        Roles r = new Roles();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM roles WHERE id = ?");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    //update role
    public static void updateRole(Roles r) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("UPDATE roles SET name = ? WHERE id = ?");
            pstm.setString(1, r.getName());
            pstm.setInt(2, r.getId());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //delete role
    public static void deleteRole(int id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM roles WHERE id = ?");
            pstm.setInt(1, id);
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //add role
    public static void addRole(Roles r) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO roles(name) VALUES(?)");
            pstm.setString(1, r.getName());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //main method for testing
    public static void main(String[] args) {
        //test getAllRoles
        ArrayList<Roles> roles = getAllRoles();
        for (Roles r : roles) {
            System.out.println(r);
        }

        //test getRoleById
        Roles r = getRoleById(1);
        System.out.println(r);

//        //test updateRole
//        Roles r1 = new Roles(1, "Admin");
//        updateRole(r1);
//
//        //test deleteRole
//        deleteRole(1);
//
//        //test addRole
//        Roles r2 = new Roles(1, "Admin");
//        addRole(r2);
    }


}
