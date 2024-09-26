package dao;

import model.Tags;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TagsDAO {

    // Get all tags
    public static ArrayList<Tags> getAllTags() {
        ArrayList<Tags> tags = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM tags");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Tags t = new Tags();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                tags.add(t);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return tags;
    }

    // Get tag by id
    public static Tags getTagById(int id) {
        Tags t = new Tags();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM tags WHERE id = ?");
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

    //add tag
    public static void addTag(Tags t) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO tags(name) VALUES(?)");
            pstm.setString(1, t.getName());
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //update tag
    public static void updateTag(Tags t) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("UPDATE tags SET name = ? WHERE id = ?");
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
    //delete tag
    public static void deleteTag(int id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM tags WHERE id = ?");
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
        //test getAllTags
        ArrayList<Tags> tags = getAllTags();
        for (Tags t : tags) {
            System.out.println(t);
        }
        //test getTagById
        Tags t = getTagById(1);
        System.out.println(t);
        //test addTag
        Tags t1 = new Tags();
        t1.setName("test");
        addTag(t1);
        //test updateTag
        Tags t2 = new Tags();
        t2.setId(1);
        t2.setName("test1");
        updateTag(t2);
        //test deleteTag
        deleteTag(1);
    }
}
