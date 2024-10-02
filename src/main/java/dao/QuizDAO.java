package dao;

import entity.quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class QuizDAO {

    //get all quiz
    public List<quiz> getAllQuiz() throws SQLException, ClassNotFoundException {
        List<quiz> quizs = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM quiz";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quiz q = new quiz();
                q.setId(rs.getInt("id"));
                q.setName(rs.getString("name"));
                q.setDescription(rs.getString("description"));
                q.setCreated_at(rs.getTimestamp("created_at").toString());
                q.setUpdated_at(rs.getTimestamp("updated_at").toString());
                q.setUser_id(rs.getInt("user_id"));
                q.setType_id(rs.getInt("type_id"));
                q.setAnswer(rs.getString("answer"));
                quizs.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizs;
        }

        //get quiz by id
        public quiz getQuizById(int id) throws SQLException, ClassNotFoundException {
            Connection connection = new DBConnect().getConnection();
            String sql = "SELECT * FROM quiz WHERE id = ?";
            quiz q = new quiz();
            try {
                PreparedStatement ps = connection.prepareCall(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    q.setId(rs.getInt("id"));
                    q.setName(rs.getString("name"));
                    q.setDescription(rs.getString("description"));
                    q.setCreated_at(rs.getTimestamp("created_at").toString());
                    q.setUpdated_at(rs.getTimestamp("updated_at").toString());
                    q.setUser_id(rs.getInt("user_id"));
                    q.setType_id(rs.getInt("type_id"));
                    q.setAnswer(rs.getString("answer"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return q;
        }

    // Insert quiz and return generated ID
    public int insertQuiz(quiz q) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO quiz(name, description, user_id, type_id, answer) VALUES(?,?,?,?,?)";
        int generatedId = -1;
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, q.getName());
            ps.setString(2, q.getDescription());
            ps.setInt(3, q.getUser_id());
            ps.setInt(4, q.getType_id());
            ps.setString(5, q.getAnswer());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating quiz failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }


        //update quiz
        public boolean updateQuiz(quiz q) throws SQLException, ClassNotFoundException {
            Connection connection = new DBConnect().getConnection();
            String sql = "UPDATE quiz SET name = ?, description = ?, created_at = ?, updated_at = ?, user_id = ?, type_id = ?, answer = ? WHERE id = ?";
            try {
                PreparedStatement ps = connection.prepareCall(sql);
                ps.setString(1, q.getName());
                ps.setString(2, q.getDescription());
                ps.setString(3, q.getCreated_at());
                ps.setString(4, q.getUpdated_at());
                ps.setInt(5, q.getUser_id());
                ps.setInt(6, q.getType_id());
                ps.setString(7, q.getAnswer());
                ps.setInt(8, q.getId());
                return ps.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        //delete quiz
        public boolean deleteQuiz(int id) throws SQLException, ClassNotFoundException {
            Connection connection = new DBConnect().getConnection();
            String sql = "DELETE FROM quiz WHERE id = ?";
            try {
                PreparedStatement ps = connection.prepareCall(sql);
                ps.setInt(1, id);
                return ps.executeUpdate() == 1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        //main method
        public static void main(String[] args) throws SQLException, ClassNotFoundException {
            QuizDAO dao = new QuizDAO();
            System.out.println(dao.getAllQuiz());
            System.out.println(dao.getQuizById(1));
            System.out.println(dao.insertQuiz(new quiz("name", "description", "created_at", "updated_at", 1, 1, "answer")));
            System.out.println(dao.updateQuiz(new quiz(1, "name", "description", "created_at", "updated_at", 1, 1, "answer", true)));
            System.out.println(dao.deleteQuiz(1));
        }


}
