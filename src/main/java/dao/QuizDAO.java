package dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Tag;
import entity.quiz;

import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Module.DBConnect;
import Module.AnswersReader;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class QuizDAO {



    //getTypeIdByName
    public int getTypeIdByName(String name) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT id FROM type WHERE name = ?";
        int typeId = -1;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                typeId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeId;
    }

    public List<Map<String, Object>> getAllTags() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<Map<String, Object>> tagList = new ArrayList<>();
        String SELECT_ALL_TAGS_QUERY = "SELECT * FROM tag";
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TAGS_QUERY)) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Map<String, Object> tag = new java.util.HashMap<>();
                tag.put("id", rs.getInt("id"));
                tag.put("name", rs.getString("name"));
                tag.put("description", rs.getString("description"));
                tagList.add(tag);
            }
        }
        return tagList;
    }


    //getAssignedQuizzesByClassId
    public List<quiz> getAssignedQuizzesByClassId(int classId) throws SQLException, ClassNotFoundException {
        List<quiz> quizzes = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT q.* FROM quiz q " +
                "INNER JOIN class_quiz cq ON q.id = cq.quiz_id " +
                "WHERE cq.class_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quiz q = extractQuizFromResultSet(rs);
                quizzes.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

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
        quiz q = null;
        try {
            connection.setAutoCommit(false); // Start transaction
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                q = extractQuizFromResultSet(rs);
                // Fetch tags and set them
                List<Tag> tags = getTagsByQuizId(id);
                q.setTag(tags);
                // Increment views count
                incrementQuizViews(id, connection);
            }
            connection.commit(); // Commit transaction
            ps.close();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback in case of error
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return q;
    }




    // Method to increment the views count of a quiz
    private void incrementQuizViews(int quizId, Connection connection) throws SQLException {
        String updateViewsSql = "UPDATE quiz SET views = views + 1 WHERE id = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateViewsSql);
        updateStmt.setInt(1, quizId);
        updateStmt.executeUpdate();
        updateStmt.close();
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

    public boolean updateQuizForEditQuiz(quiz q, String[] selectedTags) throws SQLException, ClassNotFoundException {
        String updateQuizSQL = "UPDATE quiz SET name = ?, description = ?, type_id = ?, answer = ?, updated_at = NOW() WHERE id = ?";
        String deleteTagsSQL = "DELETE FROM quiz_tag WHERE quiz_id = ?";
        String insertTagSQL = "INSERT INTO quiz_tag (quiz_id, tag_id) VALUES (?, ?)";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuizSQL);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteTagsSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertTagSQL)) {

            conn.setAutoCommit(false); // Bắt đầu transaction

            // Cập nhật thông tin quiz, bao gồm cả trường answer
            updateStmt.setString(1, q.getName());
            updateStmt.setString(2, q.getDescription());
            updateStmt.setInt(3, q.getType_id());
            updateStmt.setString(4, q.getAnswer()); // Đã chứa JSON
            updateStmt.setInt(5, q.getId());

            int affectedRows = updateStmt.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Updating quiz failed, no rows affected.");
            }

            // Xóa các tag hiện tại
            deleteStmt.setInt(1, q.getId());
            deleteStmt.executeUpdate();

            // Thêm các tag mới nếu có
            if (selectedTags != null && selectedTags.length > 0) {
                for (String tagIdStr : selectedTags) {
                    int tagId = Integer.parseInt(tagIdStr);
                    insertStmt.setInt(1, q.getId());
                    insertStmt.setInt(2, tagId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức cập nhật quiz (có thể loại bỏ hoặc giữ tùy nhu cầu)
    public boolean updateQuiz(quiz q, String[] selectedTags) throws SQLException, ClassNotFoundException {
        return updateQuizForEditQuiz(q, selectedTags);
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

    // Method to retrieve the latest 10 quizzes
    public List<quiz> getLatestQuizzes() throws SQLException, ClassNotFoundException {
        List<quiz> latestQuizzes = new ArrayList<>();
        Connection conn = new DBConnect().getConnection();
        String sql = "SELECT * FROM quiz ORDER BY created_at DESC LIMIT 10";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            quiz quiz = extractQuizFromResultSet(rs);
            latestQuizzes.add(quiz);
        }
        conn.close();
        return latestQuizzes;
    }


    // Method to retrieve the most popular 10 quizzes
    public List<quiz> getPopularQuizzes() throws SQLException, ClassNotFoundException {
        List<quiz> popularQuizzes = new ArrayList<>();
        Connection conn = new DBConnect().getConnection();
        String sql = "SELECT * FROM quiz ORDER BY views DESC LIMIT 10";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            quiz q = extractQuizFromResultSet(rs);
            popularQuizzes.add(q);
        }
        conn.close();
        return popularQuizzes;
    }



    // Method to retrieve all quizzes with pagination
    public List<quiz> getAllQuizzes(int offset, int limit) throws SQLException, ClassNotFoundException {
        List<quiz> allQuizzes = new ArrayList<>();
        Connection conn = new DBConnect().getConnection();
        String sql = "SELECT * FROM quiz LIMIT ? OFFSET ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, limit);
        stmt.setInt(2, offset);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            quiz quiz = extractQuizFromResultSet(rs);
            allQuizzes.add(quiz);
        }
        conn.close();
        return allQuizzes;
    }

    public List<AnswersReader> getCorrectbyId(int id) {
        List<AnswersReader> correctAnswers = new ArrayList<>();
        Connection connection = null;
        try {
            connection = new DBConnect().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String sql = "SELECT answer FROM quiz WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            ObjectMapper mapper = new ObjectMapper(); // Sử dụng Jackson ObjectMapper
            if (rs.next()) { // Chỉ lấy một bản ghi
                String answers = rs.getString("answer");
                JsonNode rootNode = mapper.readTree(answers);
                for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
                    String key = it.next();
                    JsonNode obj = rootNode.get(key);
                    String correct = obj.get("correct").asText();
                    List<String> options = new ArrayList<>();
                    for (JsonNode optionNode : obj.get("options")) {
                        options.add(optionNode.asText());
                    }
                    String question = obj.get("question").asText();
                    correctAnswers.add(new AnswersReader(Integer.parseInt(key), correct, options, question));
                }
            }
            ps.close();
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return correctAnswers;
    }


    public int getTotalQuizCount() throws SQLException, ClassNotFoundException {
        int totalQuizzes = 0;
        Connection conn = new DBConnect().getConnection();
        String sql = "SELECT COUNT(*) AS total FROM quiz";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            totalQuizzes = rs.getInt("total");
        }
        rs.close();
        stmt.close();
        conn.close();
        return totalQuizzes;
    }

    // Helper method to extract quiz from ResultSet
    private quiz extractQuizFromResultSet(ResultSet rs) throws SQLException {
        quiz q = new quiz();
        q.setId(rs.getInt("id"));
        q.setName(rs.getString("name"));
        q.setDescription(rs.getString("description"));
        q.setCreated_at(rs.getTimestamp("created_at").toString());
        q.setUpdated_at(rs.getTimestamp("updated_at").toString());
        q.setUser_id(rs.getInt("user_id"));
        q.setType_id(rs.getInt("type_id"));
        q.setAnswer(rs.getString("answer"));
        q.setStatus(rs.getBoolean("status"));
        q.setViews(rs.getInt("views")); // Include 'views'
        return q;
    }


    //    get answer by id
    public String getAnswerById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT answer FROM quiz WHERE id = ?";
        String answer = "";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                answer = rs.getString("answer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answer;

    }
    public List<quiz> getQuizzesByUserId(int userId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM quiz WHERE user_id = ?";
        List<quiz> quizzes = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quiz quiz = new quiz();
                    quiz.setId(rs.getInt("id"));
                    quiz.setName(rs.getString("name"));
                    quiz.setDescription(rs.getString("description"));
                    quizzes.add(quiz);
                }
            }
        }
        return quizzes;
    }

    public List<Tag> getTagsByQuizId(int quizId) throws SQLException, ClassNotFoundException {
        List<Tag> tags = new ArrayList<>();
        Connection conn = new DBConnect().getConnection();
        String sql = "SELECT t.id, t.name, t.description FROM tag t " +
                "INNER JOIN quiz_tag qt ON t.id = qt.tag_id " +
                "WHERE qt.quiz_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, quizId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Tag tag = new Tag();
            tag.setId(rs.getInt("id"));
            tag.setName(rs.getString("name"));
            tag.setDescription(rs.getString("description"));
            tags.add(tag);
        }
        stmt.close();
        conn.close();
        return tags;
    }

    public boolean insertQuizTag(int quizId, int tagId) throws SQLException, ClassNotFoundException {
        Connection conn = new DBConnect().getConnection();
        String sql = "INSERT INTO quiz_tag(quiz_id, tag_id) VALUES(?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, quizId);
        stmt.setInt(2, tagId);
        int affectedRows = stmt.executeUpdate();
        stmt.close();
        conn.close();
        return affectedRows == 1;
    }


}