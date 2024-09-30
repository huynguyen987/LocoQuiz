package dao;

import entity.type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;


public class TypeDAO {

    //get all type
    public List<type> getAllType() throws SQLException, ClassNotFoundException {
        List<type> types = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM type";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                type t = new type();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                types.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    //get type by id
    public type getTypeById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM type WHERE id = ?";
        type t = new type();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    //insert type
    public boolean insertType(type t) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO type(name) VALUES(?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, t.getName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update type
    public boolean updateType(type t) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE type SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, t.getName());
            ps.setInt(2, t.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete type
    public boolean deleteType(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM type WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        TypeDAO dao = new TypeDAO();
        System.out.println(dao.getAllType());
    }


}
