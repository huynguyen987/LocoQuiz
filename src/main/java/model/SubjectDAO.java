package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.*;

public class SubjectDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/learningmanagementsystem"; // Cập nhật URL DB
    private String jdbcUsername = "root"; // Cập nhật username DB
    private String jdbcPassword = "12345678"; // Cập nhật password DB

    private static final String SELECT_ALL_SUBJECTS = "SELECT * FROM Subjects";
    private static final String SELECT_SUBJECT_BY_ID = "SELECT * FROM Subjects WHERE id = ?";
    private static final String INSERT_SUBJECT = "INSERT INTO Subjects (title, description, category, thumbnail_url, status, created_by, avatar) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SUBJECT = "UPDATE Subjects SET title = ?, description = ?, category = ?, thumbnail_url = ?, status = ?, created_by = ?, avatar = ? WHERE id = ?";
    private static final String DELETE_SUBJECT = "DELETE FROM Subjects WHERE id = ?";

    private LessonDAO lessonDAO;

    public SubjectDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Đảm bảo JDBC driver có sẵn
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.lessonDAO = new LessonDAO();
    }
    // Lấy tất cả các môn học
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUBJECTS)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String thumbnailUrl = rs.getString("thumbnail_url");
                String status = rs.getString("status");
                int createdBy = rs.getInt("created_by");
                byte[] avatar = rs.getBytes("avatar");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy danh sách các bài học cho môn học này
                List<Lessons> lessons = lessonDAO.getLessonsBySubjectId(id);

                Subject subject = new Subject(id, title, description, category, thumbnailUrl,
                        status, createdBy, avatar, createdAt, updatedAt, lessons);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    // Lấy một môn học theo ID
    public Subject getSubjectById(int id) {
        Subject subject = null;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBJECT_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String thumbnailUrl = rs.getString("thumbnail_url");
                String status = rs.getString("status");
                int createdBy = rs.getInt("created_by");
                byte[] avatar = rs.getBytes("avatar");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                // Lấy danh sách các bài học cho môn học này
                List<Lessons> lessons = lessonDAO.getLessonsBySubjectId(id);

                subject = new Subject(id, title, description, category, thumbnailUrl,
                        status, createdBy, avatar, createdAt, updatedAt, lessons);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subject;
    }

    // Thêm một môn học mới
    public boolean addSubject(Subject subject) {
        boolean rowInserted = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBJECT)) {

            preparedStatement.setString(1, subject.getTitle());
            preparedStatement.setString(2, subject.getDescription());
            preparedStatement.setString(3, subject.getCategory());
            preparedStatement.setString(4, subject.getThumbnailUrl());
            preparedStatement.setString(5, subject.getStatus());
            preparedStatement.setInt(6, subject.getCreatedBy());

            if (subject.getAvatar() != null) {
                preparedStatement.setBytes(7, subject.getAvatar());
            } else {
                preparedStatement.setNull(7, java.sql.Types.BLOB);
            }

            rowInserted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowInserted;
    }

    // Cập nhật một môn học
    public boolean updateSubject(Subject subject) {
        boolean rowUpdated = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUBJECT)) {

            preparedStatement.setString(1, subject.getTitle());
            preparedStatement.setString(2, subject.getDescription());
            preparedStatement.setString(3, subject.getCategory());
            preparedStatement.setString(4, subject.getThumbnailUrl());
            preparedStatement.setString(5, subject.getStatus());
            preparedStatement.setInt(6, subject.getCreatedBy());

            if (subject.getAvatar() != null) {
                preparedStatement.setBytes(7, subject.getAvatar());
            } else {
                preparedStatement.setNull(7, java.sql.Types.BLOB);
            }

            preparedStatement.setInt(8, subject.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowUpdated;
    }

    // Xóa một môn học
    public boolean deleteSubject(int id) {
        boolean rowDeleted = false;

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBJECT)) {

            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowDeleted;
    }
}
