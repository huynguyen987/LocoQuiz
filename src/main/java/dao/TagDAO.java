package dao;

import Module.DBConnect;
import entity.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TagDAO {

    // Lấy tất cả các Tag
    public List<Tag> getAllTags() throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tag";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));
                tags.add(tag);
            }
        }
        return tags;
    }

    //deleteQuizTags
    public boolean deleteQuizTags(int quizId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM quiz_tag WHERE quiz_id = ?";
        boolean rowDeleted;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quizId);
            rowDeleted = ps.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Lấy Tag theo id
    public Tag getTagById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tag WHERE id = ?";
        Tag tag = null;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setName(rs.getString("name"));
                    tag.setDescription(rs.getString("description"));
                }
            }
        }
        return tag;
    }

    // Lấy Tag theo tên
    public Tag getTagByName(String name) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tag WHERE name = ?";
        Tag tag = null;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setName(rs.getString("name"));
                    tag.setDescription(rs.getString("description"));
                }
            }
        }
        return tag;
    }

    // Thêm Tag mới
    public boolean insertTag(Tag tag) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tag (name, description) VALUES (?, ?)";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, tag.getName());
            ps.setString(2, tag.getDescription());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public int insertTagg(Tag tag) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tag(name, description) VALUES(?, ?)";
        int generatedId = -1;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tag.getName());
            ps.setString(2, tag.getDescription());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting tag failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    tag.setId(generatedId);
                } else {
                    throw new SQLException("Inserting tag failed, no ID obtained.");
                }
            }
        }
        return generatedId;
    }


    // Cập nhật Tag
    public boolean updateTag(Tag tag) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tag SET name = ?, description = ? WHERE id = ?";
        boolean rowUpdated;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, tag.getName());
            ps.setString(2, tag.getDescription());
            ps.setInt(3, tag.getId());
            rowUpdated = ps.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    // Xóa Tag
    public boolean deleteTag(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tag WHERE id = ?";
        boolean rowDeleted;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            rowDeleted = ps.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    // Tìm kiếm Tag theo tên
    public List<Tag> searchTagByName(String name) throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tag WHERE name LIKE ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tag tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setName(rs.getString("name"));
                    tag.setDescription(rs.getString("description"));
                    tags.add(tag);
                }
            }
        }
        return tags;
    }
    public List<Tag> getFixedTags() throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT * FROM tag WHERE name IN ('Khoa Học', 'Xã Hội')";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));
                tags.add(tag);
            }
        }
        return tags;
    }

}
