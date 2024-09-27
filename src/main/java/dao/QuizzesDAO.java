package dao;

import model.Quizzes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Module.*;
import java.util.List;
import java.util.logging.Level;

public class QuizzesDAO {
    //get all quizzes
    public static List<Quizzes> getAllQuizzes() throws SQLException, ClassNotFoundException {
        List<Quizzes> quizzes = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM quizzes";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new Quizzes(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("created_at"), rs.getString("updated_at"), rs.getInt("user_id"), rs.getInt("type_id"), rs.getString("answer")));
            }
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(QuizzesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quizzes;
    }

    //get quiz by id
    public static Quizzes getQuizById(int id) throws SQLException, ClassNotFoundException {
        Quizzes quiz = null;
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM quizzes WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quiz = new Quizzes(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("created_at"), rs.getString("updated_at"), rs.getInt("user_id"), rs.getInt("type_id"), rs.getString("answer"));
            }
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(QuizzesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quiz;
    }

    //add quizzes
    public static void addQuizzes(Quizzes quiz) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO quizzes(name, description, created_at, updated_at, user_id, type_id, answer) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, quiz.getName());
            ps.setString(2, quiz.getDescription());
            ps.setString(3, quiz.getCreated_at());
            ps.setString(4, quiz.getUpdated_at());
            ps.setInt(5, quiz.getUser_id());
            ps.setInt(6, quiz.getType_id());
            ps.setString(7, quiz.getAnswer());
            ps.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(QuizzesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update quizzes
    public static void updateQuizzes(Quizzes quiz) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE quizzes SET name = ?, description = ?, updated_at = ?, user_id = ?, type_id = ?, answer = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, quiz.getName());
            ps.setString(2, quiz.getDescription());
            ps.setString(3, quiz.getUpdated_at());
            ps.setInt(4, quiz.getUser_id());
            ps.setInt(5, quiz.getType_id());
            ps.setString(6, quiz.getAnswer());
            ps.setInt(7, quiz.getId());
            ps.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(QuizzesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //delete quizzes
    public static void deleteQuizzes(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM quizzes WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(QuizzesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //test getAllQuizzes
        List<Quizzes> quizzes = getAllQuizzes();
        for (Quizzes quiz : quizzes) {
            System.out.println(quiz);
        }
    }
}
