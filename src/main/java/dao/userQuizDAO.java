package dao;

import entity.user_quiz;
import Module.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userQuizDAO {

    // Lấy tất cả các user_quiz
    public List<user_quiz> getAllUserQuiz() throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT * FROM user_quiz";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                user_quiz userQuiz = new user_quiz();
                userQuiz.setUser_id(rs.getInt("user_id"));
                userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                userQuiz.setTag_id(rs.getInt("tag_id"));
                userQuizList.add(userQuiz);
            }
        }
        return userQuizList;
    }

    // Lấy user_quiz theo user_id, quiz_id, tag_id
    public user_quiz getUserQuiz(int user_id, int quiz_id, int tag_id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM user_quiz WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        user_quiz userQuiz = null;
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, tag_id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                }
            }
        }
        return userQuiz;
    }

    // Thêm user_quiz mới
    public boolean insertUserQuiz(user_quiz userQuiz) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO user_quiz (user_id, quiz_id, tag_id) VALUES (?, ?, ?)";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userQuiz.getUser_id());
            ps.setInt(2, userQuiz.getQuiz_id());
            ps.setInt(3, userQuiz.getTag_id());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Cập nhật user_quiz
    public boolean updateUserQuiz(user_quiz oldUserQuiz, user_quiz newUserQuiz) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE user_quiz SET user_id = ?, quiz_id = ?, tag_id = ? WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Thiết lập giá trị mới
            ps.setInt(1, newUserQuiz.getUser_id());
            ps.setInt(2, newUserQuiz.getQuiz_id());
            ps.setInt(3, newUserQuiz.getTag_id());

            // Điều kiện WHERE
            ps.setInt(4, oldUserQuiz.getUser_id());
            ps.setInt(5, oldUserQuiz.getQuiz_id());
            ps.setInt(6, oldUserQuiz.getTag_id());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Xóa user_quiz
    public boolean deleteUserQuiz(int user_id, int quiz_id, int tag_id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM user_quiz WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, tag_id);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Lấy danh sách user_quiz theo user_id
    public List<user_quiz> getUserQuizByUserId(int user_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT * FROM user_quiz WHERE user_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user_quiz userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                    userQuizList.add(userQuiz);
                }
            }
        }
        return userQuizList;
    }

    // Lấy danh sách user_quiz theo quiz_id
    public List<user_quiz> getUserQuizByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT * FROM user_quiz WHERE quiz_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quiz_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user_quiz userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                    userQuizList.add(userQuiz);
                }
            }
        }
        return userQuizList;
    }

    // Lấy danh sách user_quiz theo tag_id
    public List<user_quiz> getUserQuizByTagId(int tag_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT * FROM user_quiz WHERE tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, tag_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user_quiz userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                    userQuizList.add(userQuiz);
                }
            }
        }
        return userQuizList;
    }

    // Kiểm tra sự tồn tại của user_quiz
    public boolean existsUserQuiz(int user_id, int quiz_id, int tag_id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT 1 FROM user_quiz WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, tag_id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        userQuizDAO userQuizDAO = new userQuizDAO();
        List<user_quiz> userQuizList = userQuizDAO.getAllUserQuiz();
        for (user_quiz userQuiz : userQuizList) {
            System.out.println(userQuiz.getUser_id() + " - " + userQuiz.getQuiz_id() + " - " + userQuiz.getTag_id());
        }
    }
}
