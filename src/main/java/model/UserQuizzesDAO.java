package model;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import entity.UserQuizzes;
import Module.DBConnect;

public class UserQuizzesDAO {
    // Add user quiz
    public void addUserQuiz(UserQuizzes userQuiz) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String query = "INSERT INTO UserQuizzes (user_id, quiz_id, start_time, end_time, score, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userQuiz.getUserId());
            preparedStatement.setInt(2, userQuiz.getQuizId());
            preparedStatement.setTimestamp(3, userQuiz.getStartTime());
            preparedStatement.setTimestamp(4, userQuiz.getEndTime());
            preparedStatement.setBigDecimal(5, userQuiz.getScore());
            preparedStatement.setString(6, userQuiz.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Get user quiz by id
    public UserQuizzes getUserQuizById(int id) throws SQLException, ClassNotFoundException {
        UserQuizzes userQuiz = null;
        Connection connection = new DBConnect().getConnection();
        String query = "SELECT * FROM UserQuizzes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userQuiz = new UserQuizzes();
                    userQuiz.setId(resultSet.getInt("id"));
                    userQuiz.setUserId(resultSet.getInt("user_id"));
                    userQuiz.setQuizId(resultSet.getInt("quiz_id"));
                    userQuiz.setStartTime(resultSet.getTimestamp("start_time"));
                    userQuiz.setEndTime(resultSet.getTimestamp("end_time"));
                    userQuiz.setScore(resultSet.getBigDecimal("score"));
                    userQuiz.setStatus(resultSet.getString("status"));
                    userQuiz.setCreatedAt(resultSet.getTimestamp("created_at"));
                    userQuiz.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userQuiz;
    }

    // Get all user quizzes
    public ArrayList<UserQuizzes> getAllUserQuizzes() throws SQLException, ClassNotFoundException {
        ArrayList<UserQuizzes> userQuizzes = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String query = "SELECT * FROM UserQuizzes";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                UserQuizzes userQuiz = new UserQuizzes();
                userQuiz.setId(resultSet.getInt("id"));
                userQuiz.setUserId(resultSet.getInt("user_id"));
                userQuiz.setQuizId(resultSet.getInt("quiz_id"));
                userQuiz.setStartTime(resultSet.getTimestamp("start_time"));
                userQuiz.setEndTime(resultSet.getTimestamp("end_time"));
                userQuiz.setScore(resultSet.getBigDecimal("score"));
                userQuiz.setStatus(resultSet.getString("status"));
                userQuiz.setCreatedAt(resultSet.getTimestamp("created_at"));
                userQuiz.setUpdatedAt(resultSet.getTimestamp("updated_at"));
                userQuizzes.add(userQuiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userQuizzes;
    }

    // Update user quiz
    public void updateUserQuiz(UserQuizzes userQuiz) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String query = "UPDATE UserQuizzes SET user_id = ?, quiz_id = ?, start_time = ?, end_time = ?, score = ?, status = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userQuiz.getUserId());
            preparedStatement.setInt(2, userQuiz.getQuizId());
            preparedStatement.setTimestamp(3, userQuiz.getStartTime());
            preparedStatement.setTimestamp(4, userQuiz.getEndTime());
            preparedStatement.setBigDecimal(5, userQuiz.getScore());
            preparedStatement.setString(6, userQuiz.getStatus());
            preparedStatement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(8, userQuiz.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete user quiz
    public void deleteUserQuiz(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String query = "DELETE FROM UserQuizzes WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //main method for testing
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserQuizzesDAO dao = new UserQuizzesDAO();
        ArrayList<UserQuizzes> userQuizzes = dao.getAllUserQuizzes();
        for (UserQuizzes userQuiz : userQuizzes) {
            System.out.println(userQuiz.getId() + " " + userQuiz.getScore());
        }
    }
}
