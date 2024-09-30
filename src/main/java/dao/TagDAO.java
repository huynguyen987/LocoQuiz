package dao;

import Module.DBConnect;
import entity.tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TagDAO {

    //get all tags
    public List<tag> getAllTags() throws SQLException, ClassNotFoundException {
        List<tag> tags = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM tag";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tag tag = new tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    //get tag by id
    public tag getTagById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM tag WHERE id = ?";
        tag tag = new tag();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    //get tag by name
    public tag getTagByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM tag WHERE name = ?";
        tag tag = new tag();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    //insert tag
    public boolean insertTag(tag tag) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO tag(name) VALUES(?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, tag.getName());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update tag
    public boolean updateTag(tag tag) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE tag SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setString(1, tag.getName());
            ps.setInt(2, tag.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete tag
    public void deleteTag(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM tag WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        TagDAO dao = new TagDAO();
        System.out.println(dao.getAllTags());
    }


}
