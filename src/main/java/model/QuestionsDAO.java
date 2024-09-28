package model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entity.Questions;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import Module.DBConnect;
import entity.Questions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QuestionsDAO extends DBConnect {

    //get all questions
    public List<Questions> getAllQuestions() {
        List<Questions> questions = new ArrayList<>();
        try {
            Connection connection = getConnection();
            String sql = "SELECT * FROM questions";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int quizId = rs.getInt("quiz_id");
                String content = rs.getString("content");
                String questionType = rs.getString("question_type");
                String difficultyLevel = rs.getString("difficulty_level");
                String explanation = rs.getString("explanation");
                String answersString = rs.getString("answers");
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                List<String> answers = new Gson().fromJson(answersString, listType);
                int order = rs.getInt("order");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                Questions question = new Questions(id, quizId, content, questionType, difficultyLevel, explanation, answers, order, createdAt, updatedAt);
                questions.add(question);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return questions;
    }
    //get questions by quiz id
    public List<Questions> getQuestionsByQuizId(int quizId) {
        List<Questions> questions = new ArrayList<>();
        try {
            Connection connection = getConnection();
            String sql = "SELECT * FROM questions WHERE quiz_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String content = rs.getString("content");
                String questionType = rs.getString("question_type");
                String difficultyLevel = rs.getString("difficulty_level");
                String explanation = rs.getString("explanation");
                String answersString = rs.getString("answers");
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                List<String> answers = new Gson().fromJson(answersString, listType);
                int order = rs.getInt("order");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                Questions question = new Questions(id, quizId, content, questionType, difficultyLevel, explanation, answers, order, createdAt, updatedAt);
                questions.add(question);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return questions;
    }
    //get question by id
    public Questions getQuestionById(int id) {
        Questions question = null;
        try {
            Connection connection = getConnection();
            String sql = "SELECT * FROM questions WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int quizId = rs.getInt("quiz_id");
                String content = rs.getString("content");
                String questionType = rs.getString("question_type");
                String difficultyLevel = rs.getString("difficulty_level");
                String explanation = rs.getString("explanation");
                String answersString = rs.getString("answers");
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                List<String> answers = new Gson().fromJson(answersString, listType);
                int order = rs.getInt("order");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                question = new Questions(id, quizId, content, questionType, difficultyLevel, explanation, answers, order, createdAt, updatedAt);
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return question;
    }
    //add question
    public void addQuestion(Questions question) {
        try {
            Connection connection = getConnection();
            String sql = "INSERT INTO questions(quiz_id, content, question_type, difficulty_level, explanation, answers, `order`) VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getContent());
            stmt.setString(3, question.getQuestionType());
            stmt.setString(4, question.getDifficultyLevel());
            stmt.setString(5, question.getExplanation());
            stmt.setString(6, new Gson().toJson(question.getAnswers()));
            stmt.setInt(7, question.getOrder());
            stmt.executeUpdate();
            stmt.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



}
