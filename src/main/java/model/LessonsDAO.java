package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import entity.Lessons;
import Module.DBConnect;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LessonsDAO {

    // Add a lesson
    public boolean addLesson(Lessons lesson) {
        int n = 0;
        String sql = "INSERT INTO Lessons(subject_id, title, content, `order`, status) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, lesson.getSubjectId()); // Assuming Lessons class has getSubjectId()
            pre.setString(2, lesson.getTitle());
            pre.setString(3, lesson.getContent());
            pre.setInt(4, lesson.getOrder()); // Assuming Lessons class has getOrder()
            pre.setString(5, lesson.getStatus());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Get lesson by ID
    public Lessons getLessonById(int id) {
        Lessons lesson = null;
        String sql = "SELECT * FROM Lessons WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    lesson = new Lessons();
                    lesson.setId(rs.getInt("id"));
                    lesson.setSubjectId(rs.getInt("subject_id")); // Assuming Lessons class has setSubjectId()
                    lesson.setTitle(rs.getString("title"));
                    lesson.setContent(rs.getString("content"));
                    lesson.setOrder(rs.getInt("order")); // Assuming Lessons class has setOrder()
                    lesson.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesson;
    }

    // Get all lessons
    public List<Lessons> getAllLessons() {
        List<Lessons> lessons = new ArrayList<>();
        String sql = "SELECT * FROM Lessons";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                Lessons lesson = new Lessons();
                lesson.setId(rs.getInt("id"));
                lesson.setSubjectId(rs.getInt("subject_id")); // Assuming Lessons class has setSubjectId()
                lesson.setTitle(rs.getString("title"));
                lesson.setContent(rs.getString("content"));
                lesson.setOrder(rs.getInt("order")); // Assuming Lessons class has setOrder()
                lesson.setStatus(rs.getString("status"));
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lessons;
    }

    // Update a lesson
    public boolean updateLesson(Lessons lesson) {
        int n = 0;
        String sql = "UPDATE Lessons SET subject_id = ?, title = ?, content = ?, `order` = ?, status = ? WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, lesson.getSubjectId()); // Assuming Lessons class has getSubjectId()
            pre.setString(2, lesson.getTitle());
            pre.setString(3, lesson.getContent());
            pre.setInt(4, lesson.getOrder()); // Assuming Lessons class has getOrder()
            pre.setString(5, lesson.getStatus());
            pre.setInt(6, lesson.getId());
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    // Remove a lesson
    public boolean removeLesson(int id) {
        int n = 0;
        String sql = "DELETE FROM Lessons WHERE id = ?";

        try (Connection conn = (new DBConnect()).getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    //main method for testing
    public static void main(String[] args) {
        LessonsDAO dao = new LessonsDAO();
        List<Lessons> lessons = dao.getAllLessons();
        for (Lessons lesson : lessons) {
            System.out.println(lesson.getId() + " " + lesson.getTitle());
        }
    }
}
