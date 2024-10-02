package dao;

import Module.DBConnect;
import entity.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserTagDAO {

    // Thêm Tag cho người dùng
    public boolean addUserTag(int userId, int tagId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO user_tag (user_id, tag_id) VALUES (?, ?)";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, tagId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Kiểm tra xem người dùng đã có Tag này chưa
    public boolean userHasTag(int userId, int tagId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM user_tag WHERE user_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, tagId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Lấy danh sách Tag của người dùng
    public List<Tag> getUserTagsByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.* FROM tag t INNER JOIN user_tag ut ON t.id = ut.tag_id WHERE ut.user_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
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

    // Tìm kiếm Tag của người dùng theo tên
    public List<Tag> searchUserTagsByName(int userId, String name) throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.* FROM tag t INNER JOIN user_tag ut ON t.id = ut.tag_id WHERE ut.user_id = ? AND t.name LIKE ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, "%" + name + "%");
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

    // Xóa Tag khỏi danh sách của người dùng
    public boolean deleteUserTag(int userId, int tagId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM user_tag WHERE user_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, tagId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    //main method
    public static void main(String[] args) {
        UserTagDAO userTagDAO = new UserTagDAO();
        try {
            // Test addUserTag
            boolean added = userTagDAO.addUserTag(1, 1);
            System.out.println(added);

            // Test userHasTag
            boolean hasTag = userTagDAO.userHasTag(1, 1);
            System.out.println(hasTag);

            // Test getUserTagsByUserId
            List<Tag> tags = userTagDAO.getUserTagsByUserId(1);
            for (Tag tag : tags) {
                System.out.println(tag);
            }

            // Test searchUserTagsByName
            List<Tag> tags2 = userTagDAO.searchUserTagsByName(1, "Khoa");
            for (Tag tag : tags2) {
                System.out.println(tag);
            }

            // Test deleteUserTag
            boolean deleted = userTagDAO.deleteUserTag(1, 1);
            System.out.println(deleted);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
