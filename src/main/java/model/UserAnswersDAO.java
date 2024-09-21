package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.UserAnswers;
import Module.DBConnect;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserAnswersDAO {
    // Add user answer
    public boolean addUserAnswer(UserAnswers userAnswer) {
        int n = 0;
        String sql = "INSERT INTO UserAnswers(user_quiz_id, question_id, selected_answer) VALUES(?, ?, ?)";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, userAnswer.getUserQuizId());
            pre.setInt(2, userAnswer.getQuestionId());
            pre.setString(3, userAnswer.getSelectedAnswer());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Get user answer by ID
    public UserAnswers getUserAnswerById(int id) {
        UserAnswers userAnswer = null;
        String sql = "SELECT * FROM UserAnswers WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    userAnswer = new UserAnswers();
                    userAnswer.setId(rs.getInt("id"));
                    userAnswer.setUserQuizId(rs.getInt("user_quiz_id"));
                    userAnswer.setQuestionId(rs.getInt("question_id"));
                    userAnswer.setSelectedAnswer(rs.getString("selected_answer"));
                    userAnswer.setCreatedAt(rs.getTimestamp("created_at"));
                    userAnswer.setUpdatedAt(rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return userAnswer;
    }

    // Get all user answers
    public List<UserAnswers> getAllUserAnswers() {
        List<UserAnswers> userAnswers = new ArrayList<>();
        String sql = "SELECT * FROM UserAnswers";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                UserAnswers userAnswer = new UserAnswers();
                userAnswer.setId(rs.getInt("id"));
                userAnswer.setUserQuizId(rs.getInt("user_quiz_id"));
                userAnswer.setQuestionId(rs.getInt("question_id"));
                userAnswer.setSelectedAnswer(rs.getString("selected_answer"));
                userAnswer.setCreatedAt(rs.getTimestamp("created_at"));
                userAnswer.setUpdatedAt(rs.getTimestamp("updated_at"));
                userAnswers.add(userAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return userAnswers;
    }

    // Update user answer
    public boolean updateUserAnswer(UserAnswers userAnswer) {
        int n = 0;
        String sql = "UPDATE UserAnswers SET user_quiz_id = ?, question_id = ?, selected_answer = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, userAnswer.getUserQuizId());
            pre.setInt(2, userAnswer.getQuestionId());
            pre.setString(3, userAnswer.getSelectedAnswer());
            pre.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis())); // Set updated_at to the current time
            pre.setInt(5, userAnswer.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    // Delete user answer
    public boolean removeUserAnswer(int id) {
        int n = 0;
        String sql = "DELETE FROM UserAnswers WHERE id = ?";

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
    //main method
    public static void main(String[] args) {
        UserAnswersDAO userAnswersDAO = new UserAnswersDAO();
        List<UserAnswers> userAnswers = userAnswersDAO.getAllUserAnswers();
        for (UserAnswers userAnswer : userAnswers) {
            System.out.println(userAnswer.getId() + " " + userAnswer.getUserQuizId() + " " + userAnswer.getQuestionId() + " " + userAnswer.getSelectedAnswer() + " " + userAnswer.getCreatedAt() + " " + userAnswer.getUpdatedAt());
        }
    }
}
