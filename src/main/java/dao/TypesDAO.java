package dao;

import model.Types;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TypesDAO {

    //get all types
    public static ArrayList<Types> getAllTypes() {
        ArrayList<Types> types = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM types");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Types t = new Types();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                types.add(t);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return types;
    }

    //get type by id
    public static Types getTypeById(int id) {
        Types t = new Types();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM types WHERE id = ?");
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    //add new type
    public static void addType(Types t) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO types(name) VALUES(?)");
            pstm.setString(1, t.getName());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //update type
    public static void updateType(Types t) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("UPDATE types SET name = ? WHERE id = ?");
            pstm.setString(1, t.getName());
            pstm.setInt(2, t.getId());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //delete type
    public static void deleteType(int id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM types WHERE id = ?");
            pstm.setInt(1, id);
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
        TypesDAO typesDAO = new TypesDAO();
        ArrayList<Types> types = typesDAO.getAllTypes();
        for (Types t : types) {
            System.out.println(t);
        }
    }

}
