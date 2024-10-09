package dao;

import Module.DBConnect;
import entity.class_quiz;
import entity.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassQuizDAO {

    //get all class_quiz
    public List<class_quiz> getAllClassQuiz() throws SQLException, ClassNotFoundException {
        List<class_quiz> class_quizzes = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_quiz";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                class_quiz cq = new class_quiz();
                cq.setClass_id(rs.getInt("class_id"));
                cq.setQuiz_id(rs.getInt("quiz_id"));
                class_quizzes.add(cq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return class_quizzes;
    }

    //get class_quiz by class_id
    public class_quiz getClassQuizByClassId(int class_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_quiz WHERE class_id = ?";
        class_quiz cq = new class_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cq.setClass_id(rs.getInt("class_id"));
                cq.setQuiz_id(rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cq;
    }

    //get class_quiz by quiz_id
    public class_quiz getClassQuizByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_quiz WHERE quiz_id = ?";
        class_quiz cq = new class_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cq.setClass_id(rs.getInt("class_id"));
                cq.setQuiz_id(rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cq;
    }

    //get class_quiz by class_id and quiz_id
    public class_quiz getClassQuizByClassIdAndQuizId(int class_id, int quiz_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM class_quiz WHERE class_id = ? AND quiz_id = ?";
        class_quiz cq = new class_quiz();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cq.setClass_id(rs.getInt("class_id"));
                cq.setQuiz_id(rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cq;
    }

    //add new class_quiz
    public boolean addClassQuiz(class_quiz cq) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO class_quiz(class_id, quiz_id) VALUES(?, ?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, cq.getClass_id());
            ps.setInt(2, cq.getQuiz_id());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update class_quiz
    public boolean updateClassQuiz(class_quiz cq) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE class_quiz SET quiz_id = ? WHERE class_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, cq.getQuiz_id());
            ps.setInt(2, cq.getClass_id());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_quiz
    public boolean deleteClassQuiz(int class_id, int quiz_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_quiz WHERE class_id = ? AND quiz_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_quiz by class_id
    public boolean deleteClassQuizByClassId(int class_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_quiz WHERE class_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, class_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete class_quiz by quiz_id
    public boolean deleteClassQuizByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM class_quiz WHERE quiz_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, quiz_id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<quiz> getQuizzesByClassId(int classId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT q.* FROM quiz q JOIN class_quiz cq ON q.id = cq.quiz_id WHERE cq.class_id = ?";
        List<quiz> quizzes = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quiz quiz = new quiz();
                    quiz.setId(rs.getInt("id"));
                    quiz.setName(rs.getString("name"));
                    quiz.setDescription(rs.getString("description"));
                    quiz.setCreated_at(rs.getTimestamp("created_at").toString());
                    quiz.setUpdated_at(rs.getTimestamp("updated_at").toString());
                    quizzes.add(quiz);
                }
            }
        }
        return quizzes;
    }

    public boolean assignQuizToClass(int classId, int quizId) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO class_quiz (class_id, quiz_id) VALUES (?, ?)";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            ps.setInt(2, quizId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
}

