package dao;

import model.UserQuizzes;
import Module.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserQuizzesDAO {

    // Get all quizzes of a user
    public static ArrayList<UserQuizzes> getAllQuizzesOfUser(int user_id) {
        ArrayList<UserQuizzes> quizzes = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM user_quizzes WHERE user_id = ?");
            pstm.setInt(1, user_id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                UserQuizzes uq = new UserQuizzes();
                uq.setUser_id(rs.getInt("user_id"));
                uq.setQuiz_id(rs.getInt("quiz_id"));
                uq.setTag_id(rs.getInt("tag_id"));
                quizzes.add(uq);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    //get all quizzes by quiz_id
    public static ArrayList<UserQuizzes> getAllQuizzesByQuizId(int quiz_id) {
        ArrayList<UserQuizzes> quizzes = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM user_quizzes WHERE quiz_id = ?");
            pstm.setInt(1, quiz_id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                UserQuizzes uq = new UserQuizzes();
                uq.setUser_id(rs.getInt("user_id"));
                uq.setQuiz_id(rs.getInt("quiz_id"));
                uq.setTag_id(rs.getInt("tag_id"));
                quizzes.add(uq);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    //get all quizzes by tag_id
    public static ArrayList<UserQuizzes> getAllQuizzesByTagId(int tag_id) {
        ArrayList<UserQuizzes> quizzes = new ArrayList<>();
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("SELECT * FROM user_quizzes WHERE tag_id = ?");
            pstm.setInt(1, tag_id);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                UserQuizzes uq = new UserQuizzes();
                uq.setUser_id(rs.getInt("user_id"));
                uq.setQuiz_id(rs.getInt("quiz_id"));
                uq.setTag_id(rs.getInt("tag_id"));
                quizzes.add(uq);
            }
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    //add a quiz to a user
    public static void addQuizToUser(int user_id, int quiz_id, int tag_id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("INSERT INTO user_quizzes(user_id, quiz_id, tag_id) VALUES(?, ?, ?)");
            pstm.setInt(1, user_id);
            pstm.setInt(2, quiz_id);
            pstm.setInt(3, tag_id);
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //delete a quiz from a user
    public static void deleteQuizFromUser(int user_id, int quiz_id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("DELETE FROM user_quizzes WHERE user_id = ? AND quiz_id = ?");
            pstm.setInt(1, user_id);
            pstm.setInt(2, quiz_id);
            pstm.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //update a quiz of a user
    public static void updateQuizOfUser(int user_id, int quiz_id, int tag_id) {
        try {
            Connection conn = new DBConnect().getConnection();
            PreparedStatement pstm = conn.prepareStatement("UPDATE user_quizzes SET tag_id = ? WHERE user_id = ? AND quiz_id = ?");
            pstm.setInt(1, tag_id);
            pstm.setInt(2, user_id);
            pstm.setInt(3, quiz_id);
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
        ArrayList<UserQuizzes> quizzes = getAllQuizzesOfUser(1);
        for (UserQuizzes quiz : quizzes) {
            System.out.println(quiz);
        }
    }



}
