package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import entity.Quizzes;
import Module.DBConnect;

public class QuizzesDAO {

    // Get all quizzes
    public List<Quizzes> getAllQuizzes() {
        List<Quizzes> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM Quizzes";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                Quizzes quiz = new Quizzes();
                quiz.setId(rs.getInt("id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setDescription(rs.getString("description"));
                quiz.setStatus(rs.getString("status"));
                quiz.setSubjectId(rs.getInt("subject_id"));
                quiz.setCreatedAt(rs.getTimestamp("created_at"));
                quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quizzes;
    }

    // Get quiz by id
    public Quizzes getQuizById(int id) {
        Quizzes quiz = null;
        String sql = "SELECT * FROM Quizzes WHERE id = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    quiz = new Quizzes();
                    quiz.setId(rs.getInt("id"));
                    quiz.setTitle(rs.getString("title"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setStatus(rs.getString("status"));
                    quiz.setSubjectId(rs.getInt("subject_id"));
                    quiz.setCreatedAt(rs.getTimestamp("created_at"));
                    quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quiz;
    }

    // Add quiz
    public boolean addQuiz(Quizzes quiz) {
        int n = 0;
        String sql = "INSERT INTO Quizzes(title, description, status, subject_id) VALUES(?, ?, ?, ?)";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, quiz.getTitle());
            pre.setString(2, quiz.getDescription());
            pre.setString(3, quiz.getStatus());
            pre.setInt(4, quiz.getSubjectId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Update quiz
    public boolean updateQuiz(Quizzes quiz) {
        int n = 0;
        String sql = "UPDATE Quizzes SET title = ?, description = ?, status = ?, subject_id = ? WHERE id = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, quiz.getTitle());
            pre.setString(2, quiz.getDescription());
            pre.setString(3, quiz.getStatus());
            pre.setInt(4, quiz.getSubjectId());
            pre.setInt(5, quiz.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Delete quiz
    public boolean deleteQuiz(int id) {
        int n = 0;
        String sql = "DELETE FROM Quizzes WHERE id = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Get quiz by title
    public Quizzes getQuizByTitle(String title) {
        Quizzes quiz = null;
        String sql = "SELECT * FROM Quizzes WHERE title = ?";
        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, title);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    quiz = new Quizzes();
                    quiz.setId(rs.getInt("id"));
                    quiz.setTitle(rs.getString("title"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setStatus(rs.getString("status"));
                    quiz.setSubjectId(rs.getInt("subject_id"));
                    quiz.setCreatedAt(rs.getTimestamp("created_at"));
                    quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quiz;
    }
    //main method
    public static void main(String[] args) {
        QuizzesDAO quizzesDAO = new QuizzesDAO();
        List<Quizzes> quizzes = quizzesDAO.getAllQuizzes();
        for (Quizzes quiz : quizzes) {
            System.out.println(quiz.getId() + " " + quiz.getTitle() + " " + quiz.getDescription() + " " + quiz.getStatus() + " " + quiz.getSubjectId() + " " + quiz.getCreatedAt() + " " + quiz.getUpdatedAt());
        }
    }
}
