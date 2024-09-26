package dao;

import model.ClassQuizzes;
import Module.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ClassQuizzesDAO {
    private Connection connection;
    private final String GET_QUIZZES_BY_CLASS = "SELECT * FROM class_quizzes WHERE class_id = ?";
    private final String GET_QUIZZES_BY_QUIZ = "SELECT * FROM class_quizzes WHERE quiz_id = ?";
    private final String ADD_QUIZ_TO_CLASS = "INSERT INTO class_quizzes (class_id, quiz_id) VALUES (?, ?)";
    private final String DELETE_QUIZ_FROM_CLASS = "DELETE FROM class_quizzes WHERE class_id = ? AND quiz_id = ?";
    private final String DELETE_QUIZZES_FROM_CLASS = "DELETE FROM class_quizzes WHERE class_id = ?";
    private final String DELETE_QUIZZES_FROM_QUIZ = "DELETE FROM class_quizzes WHERE quiz_id = ?";
    private final String GET_QUIZZES_BY_CLASS_AND_QUIZ = "SELECT * FROM class_quizzes WHERE class_id = ? AND quiz_id = ?";
    private final String GET_QUIZZES_BY_CLASS_AND_QUIZ_AND_USER = "SELECT * FROM class_quizzes WHERE class_id = ? AND quiz_id = ? AND user_id = ?";
    private final String GET_QUIZZES_BY_USER = "SELECT * FROM class_quizzes WHERE user_id = ?";
    private final String GET_QUIZZES_BY_USER_AND_CLASS = "SELECT * FROM class_quizzes WHERE user_id = ? AND class_id = ?";
    private final String GET_QUIZZES_BY_USER_AND_QUIZ = "SELECT * FROM class_quizzes WHERE user_id = ? AND quiz_id = ?";
    private final String GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ = "SELECT * FROM class_quizzes WHERE user_id = ? AND class_id = ? AND quiz_id = ?";
    private final String GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ_AND_SCORE = "SELECT * FROM class_quizzes WHERE user_id = ? AND class_id = ? AND quiz_id = ? AND score = ?";
    private final String GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ_AND_SCORE_AND_CREATED_AT = "SELECT * FROM class_quizzes WHERE user_id = ? AND class_id = ? AND quiz_id = ? AND score = ? AND created_at = ?";
    private final String GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ_AND_CREATED_AT = "SELECT * FROM class_quizzes WHERE user_id = ? AND class_id = ? AND quiz";

    //
    public ClassQuizzesDAO() throws SQLException, ClassNotFoundException {
        connection = new DBConnect().getConnection();
    }

    // Get quizzes by class and quiz
    public ArrayList<ClassQuizzes> getQuizzesByClass(int class_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_CLASS);
            ps.setInt(1, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
     //method to get quizzes by quiz
    public ArrayList<ClassQuizzes> getQuizzesByQuiz(int quiz_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_QUIZ);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
         //method to add quiz to class
    public boolean addQuizToClass(int class_id, int quiz_id) {
        try {
            PreparedStatement ps = connection.prepareStatement(ADD_QUIZ_TO_CLASS);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
        //method to delete quiz from class
    public boolean deleteQuizFromClass(int class_id, int quiz_id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_QUIZ_FROM_CLASS);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete quizzes
    public boolean deleteQuizzesFromClass(int class_id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_QUIZZES_FROM_CLASS);
            ps.setInt(1, class_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
  //method to delete quizzes from quiz
    public boolean deleteQuizzesFromQuiz(int quiz_id) {
        try {
            PreparedStatement ps = connection.prepareStatement(DELETE_QUIZZES_FROM_QUIZ);
            ps.setInt(1, quiz_id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get quizzes by class and quiz
    public ClassQuizzes getQuizzesByClassAndQuiz(int class_id, int quiz_id) {
        ClassQuizzes classQuizzes = null;
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_CLASS_AND_QUIZ);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                classQuizzes = new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classQuizzes;
    }

    // Get quizzes by class, quiz and user
    public ClassQuizzes getQuizzesByClassAndQuizAndUser(int class_id, int quiz_id, int user_id) {
        ClassQuizzes classQuizzes = null;
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_CLASS_AND_QUIZ_AND_USER);
            ps.setInt(1, class_id);
            ps.setInt(2, quiz_id);
            ps.setInt(3, user_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                classQuizzes = new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classQuizzes;
    }

    // Get quizzes by user
    public ArrayList<ClassQuizzes> getQuizzesByUser(int user_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // Get quizzes by user and class
    public ArrayList<ClassQuizzes> getQuizzesByUserAndClass(int user_id, int class_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER_AND_CLASS);
            ps.setInt(1, user_id);
            ps.setInt(2, class_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
    // Get quizzes by user and quiz
    public ArrayList<ClassQuizzes> getQuizzesByUserAndQuiz(int user_id, int quiz_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER_AND_QUIZ);
            ps.setInt(1, user_id);
            ps.setInt(2, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // Get quizzes by user and class and quiz

    public ArrayList<ClassQuizzes> getQuizzesByUserAndClassAndQuiz(int user_id, int class_id, int quiz_id) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ);
            ps.setInt(1, user_id);
            ps.setInt(2, class_id);
            ps.setInt(3, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // Get quizzes by user and class and quiz and score
    public ArrayList<ClassQuizzes> getQuizzesByUserAndClassAndQuizAndScore(int user_id, int class_id, int quiz_id, double score) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ_AND_SCORE);
            ps.setInt(1, user_id);
            ps.setInt(2, class_id);
            ps.setInt(3, quiz_id);
            ps.setDouble(4, score);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // Get quizzes by user and class and quiz and score and created_at

    public ArrayList<ClassQuizzes> getQuizzesByUserAndClassAndQuizAndScoreAndCreatedAt(int user_id, int class_id, int quiz_id, double score, String created_at) {
        ArrayList<ClassQuizzes> quizzes = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_QUIZZES_BY_USER_AND_CLASS_AND_QUIZ_AND_SCORE_AND_CREATED_AT);
            ps.setInt(1, user_id);
            ps.setInt(2, class_id);
            ps.setInt(3, quiz_id);
            ps.setDouble(4, score);
            ps.setString(5, created_at);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quizzes.add(new ClassQuizzes(rs.getInt("class_id"), rs.getInt("quiz_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;

    }

    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ClassQuizzesDAO classQuizzesDAO = new ClassQuizzesDAO();
        System.out.println(classQuizzesDAO.getQuizzesByClass(1));
        System.out.println(classQuizzesDAO.getQuizzesByQuiz(1));
        System.out.println(classQuizzesDAO.addQuizToClass(1, 1));
        System.out.println(classQuizzesDAO.deleteQuizFromClass(1, 1));
        System.out.println(classQuizzesDAO.deleteQuizzesFromClass(1));
        System.out.println(classQuizzesDAO.deleteQuizzesFromQuiz(1));
        System.out.println(classQuizzesDAO.getQuizzesByClassAndQuiz(1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByClassAndQuizAndUser(1, 1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByUser(1));
        System.out.println(classQuizzesDAO.getQuizzesByUserAndClass(1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByUserAndQuiz(1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByUserAndClassAndQuiz(1, 1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByUserAndClassAndQuizAndScore(1, 1, 1, 1));
        System.out.println(classQuizzesDAO.getQuizzesByUserAndClassAndQuizAndScoreAndCreatedAt(1, 1, 1, 1, "2021-01-01"));
    }

}
