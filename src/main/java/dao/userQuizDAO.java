package dao;

import entity.user_quiz;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class userQuizDAO {

    //get all user_quiz
    public List<user_quiz> getAllUserQuiz() throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz ORDER BY user_id, quiz_id, tag_id";

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

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return userQuizList;
    }


    //Check quiz exists
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //get all user_quiz by id
    public user_quiz getUserQuizById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE id = ?";
        user_quiz userquiz = new user_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userquiz.setTag_id(rs.getInt("tag_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userquiz;
    }

    //insert user_quiz
    public boolean insertUserQuiz(int userId, int quizId, int tagId) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO user_quiz(user_id, quiz_id, tag_id) VALUES(?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.setInt(3, tagId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a user_quiz record
    public boolean deleteUserQuiz(int userId, int quizId, int tagId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM user_quiz WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.setInt(3, tagId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //update user_quiz
    public boolean updateUserQuiz(int oldUserId, int oldQuizId, int oldTagId, int newUserId, int newQuizId, int newTagId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE user_quiz SET user_id = ?, quiz_id = ?, tag_id = ? WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newUserId);
            ps.setInt(2, newQuizId);
            ps.setInt(3, newTagId);
            ps.setInt(4, oldUserId);
            ps.setInt(5, oldQuizId);
            ps.setInt(6, oldTagId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //get user_quiz by user_id
    public List<user_quiz> getUserQuizByUserId(int user_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz WHERE user_id = ? ORDER BY quiz_id, tag_id";

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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return userQuizList;
    }

    //get user_quiz by quiz_id
    public List<user_quiz> getUserQuizByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz WHERE quiz_id = ? ORDER BY user_id, tag_id";

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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return userQuizList;
    }

    //get user_quiz by tag_id
    public List<user_quiz> getUserQuizByTagId(int tag_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuizList = new ArrayList<>();
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz WHERE tag_id = ? ORDER BY user_id, quiz_id";

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
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return userQuizList;
    }

    //get user_quiz by user_id and quiz_id
    public user_quiz getUserQuizByUserIdAndQuizId(int user_id, int quiz_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE user_id = ? AND quiz_id = ?";
        user_quiz userquiz = new user_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userquiz.setTag_id(rs.getInt("tag_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userquiz;
    }

    //get user_quiz by user_id and tag_id
    public user_quiz getUserQuizByUserIdAndTagId(int user_id, int tag_id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz WHERE user_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, tag_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user_quiz userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                    return userQuiz;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null; // Nếu không tìm thấy
    }
    //get user_quiz by user_id, quiz_id and tag_id
    public user_quiz getUserQuizByUserIdAndQuizIdAndTagId(int user_id, int quiz_id, int tag_id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT user_id, quiz_id, tag_id FROM user_quiz WHERE user_id = ? AND quiz_id = ? AND tag_id = ?";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, tag_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user_quiz userQuiz = new user_quiz();
                    userQuiz.setUser_id(rs.getInt("user_id"));
                    userQuiz.setQuiz_id(rs.getInt("quiz_id"));
                    userQuiz.setTag_id(rs.getInt("tag_id"));
                    return userQuiz;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null; // Nếu không tìm thấy
    }

    //main method
    public static void main(String[] args) {
        try {
            userQuizDAO userQuizDAO = new userQuizDAO();

            // Test getAllUserQuiz
            List<user_quiz> allUserQuizzes = userQuizDAO.getAllUserQuiz();
            System.out.println("All User Quizzes:");
            for (user_quiz uq : allUserQuizzes) {
                System.out.println(uq);
            }

            // Test existsUserQuiz
            boolean exists = userQuizDAO.existsUserQuiz(1, 101, 3);
            System.out.println("Exists User Quiz (1, 101, 3): " + exists);

            // Test insertUserQuiz
            boolean inserted = userQuizDAO.insertUserQuiz(1, 102, 4);
            System.out.println("Inserted User Quiz (1, 102, 4): " + inserted);

            // Test deleteUserQuiz
            boolean deleted = userQuizDAO.deleteUserQuiz(1, 102, 4);
            System.out.println("Deleted User Quiz (1, 102, 4): " + deleted);

            // Test updateUserQuiz
            boolean updated = userQuizDAO.updateUserQuiz(1, 101, 3, 1, 101, 4);
            System.out.println("Updated User Quiz from (1, 101, 3) to (1, 101, 4): " + updated);

            // Test getUserQuizByUserId
            List<user_quiz> userQuizzes = userQuizDAO.getUserQuizByUserId(1);
            System.out.println("User Quizzes for User ID 1:");
            for (user_quiz uq : userQuizzes) {
                System.out.println(uq);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}




