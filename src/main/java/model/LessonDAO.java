package model;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.*;

public class LessonDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/learningmanagementsystem"; // Cập nhật URL DB
    private String jdbcUsername = "root"; // Cập nhật username DB
    private String jdbcPassword = "12345678"; // Cập nhật password DB

    private static final String SELECT_LESSONS_BY_SUBJECT_ID = "SELECT * FROM Lessons WHERE subject_id = ? ORDER BY `order` ASC";
    private static final String INSERT_LESSON = "INSERT INTO Lessons (subject_id, title, content, `order`, status) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_LESSON = "UPDATE Lessons SET subject_id = ?, title = ?, content = ?, `order` = ?, status = ? WHERE id = ?";
    private static final String DELETE_LESSON = "DELETE FROM Lessons WHERE id = ?";

    public LessonDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Đảm bảo JDBC driver có sẵn
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    // Lấy danh sách các bài học theo subjectId
    public List<Lessons> getLessonsBySubjectId(int subjectId) {
        List<Lessons> lessons = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LESSONS_BY_SUBJECT_ID)) {

            preparedStatement.setInt(1, subjectId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                int orderNumber = rs.getInt("order");
                String status = rs.getString("status");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                Lessons lesson = new Lessons(id, subjectId, title, content, orderNumber, status, createdAt, updatedAt);
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessons;
    }

    // Thêm một bài học mới
    public boolean addLesson(Lessons lesson) {
        boolean rowInserted = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LESSON)) {

            preparedStatement.setInt(1, lesson.getSubjectId());
            preparedStatement.setString(2, lesson.getTitle());
            preparedStatement.setString(3, lesson.getContent());
            preparedStatement.setInt(4, lesson.getOrder());
            preparedStatement.setString(5, lesson.getStatus());

            rowInserted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowInserted;
    }

    // Cập nhật một bài học
    public boolean updateLesson(Lessons lesson) {
        boolean rowUpdated = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LESSON)) {

            preparedStatement.setInt(1, lesson.getSubjectId());
            preparedStatement.setString(2, lesson.getTitle());
            preparedStatement.setString(3, lesson.getContent());
            preparedStatement.setInt(4, lesson.getOrder());
            preparedStatement.setString(5, lesson.getStatus());
            preparedStatement.setInt(6, lesson.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowUpdated;
    }

    // Xóa một bài học
    public boolean deleteLesson(int id) {
        boolean rowDeleted = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LESSON)) {

            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowDeleted;
    }

}
