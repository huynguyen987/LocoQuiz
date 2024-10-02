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

    //get all user quiz
    public List<user_quiz> getAllUserQuiz() throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuiz = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT \n" +
                "    uq.user_id,\n" +
                "    u.username,          -- Assuming the users table has a 'username' column\n" +
                "    uq.quiz_id,\n" +
                "    q.quiz_title,        -- Assuming the quiz table has a 'quiz_title' column\n" +
                "    uq.tag_id,\n" +
                "    t.tag_name           -- Assuming the tag table has a 'tag_name' column\n" +
                "FROM \n" +
                "    user_quiz uq\n" +
                "INNER JOIN \n" +
                "    users u ON uq.user_id = u.id\n" +
                "INNER JOIN \n" +
                "    quiz q ON uq.quiz_id = q.id\n" +
                "INNER JOIN \n" +
                "    tag t ON uq.tag_id = t.id\n" +
                "ORDER BY \n" +
                "    u.username, q.quiz_title, t.tag_name;\n";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user_quiz userquiz = new user_quiz();
                userquiz.setUser_id(rs.getInt("id"));
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userQuiz.add(userquiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuiz;
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

    //delete user_quiz
    public void deleteUserQuiz(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM user_quiz WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //update user_quiz
    public void updateUserQuiz(user_quiz userquiz) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE user_quiz SET user_id = ?, quiz_id = ?, tag_id = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, userquiz.getUser_id());
            ps.setInt(2, userquiz.getQuiz_id());
            ps.setInt(3, userquiz.getTag_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get user_quiz by user_id
    public List<user_quiz> getUserQuizByUserId(int user_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuiz = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE user_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user_quiz userquiz = new user_quiz();
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userquiz.setTag_id(rs.getInt("tag_id"));
                userQuiz.add(userquiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuiz;
    }

    //get user_quiz by quiz_id
    public List<user_quiz> getUserQuizByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuiz = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE quiz_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user_quiz userquiz = new user_quiz();
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userquiz.setTag_id(rs.getInt("tag_id"));
                userQuiz.add(userquiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuiz;
    }

    //get user_quiz by tag_id
    public List<user_quiz> getUserQuizByTagId(int tag_id) throws SQLException, ClassNotFoundException {
        List<user_quiz> userQuiz = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE tag_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, tag_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user_quiz userquiz = new user_quiz();
                userquiz.setUser_id(rs.getInt("user_id"));
                userquiz.setQuiz_id(rs.getInt("quiz_id"));
                userquiz.setTag_id(rs.getInt("tag_id"));
                userQuiz.add(userquiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userQuiz;
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
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM user_quiz WHERE user_id = ? AND tag_id = ?";
        user_quiz userquiz = new user_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, tag_id);
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


    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        userQuizDAO userquizDAO = new userQuizDAO();
        //test getAllUserQuiz
        List<user_quiz> list = userquizDAO.getAllUserQuiz();
        for (user_quiz uq : list) {
            System.out.println(uq.getUser_id() + " - " + uq.getQuiz_id() + " - " + uq.getTag_id());
        }
//        //test getUserQuizById
//        user_quiz uq = userquizDAO.getUserQuizById(1);
//        System.out.println(uq.getUser_id() + " - " + uq.getQuiz_id() + " - " + uq.getTag_id());
//        //test insertUserQuiz
//        user_quiz uq1 = new user_quiz(1, 1, 1);
//        userquizDAO.insertUserQuiz(uq1);
//        //test deleteUserQuiz
//        userquizDAO.deleteUserQuiz(1);
//        //test updateUserQuiz
//        user_quiz uq2 = new user_quiz(1, 1, 1);
//        userquizDAO.updateUserQuiz(uq2);
        //test getUserQuizByUserId
        List<user_quiz> list1 = userquizDAO.getUserQuizByUserId(1);
        for (user_quiz uq3 : list1) {
            System.out.println(uq3.getUser_id() + " - " + uq3.getQuiz_id() + " - " + uq3.getTag_id());
        }
    }


}

