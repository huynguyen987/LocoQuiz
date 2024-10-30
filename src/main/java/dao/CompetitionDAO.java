package dao;

import entity.Competition;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class CompetitionDAO {

    // Định dạng thời gian
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Tạo một cuộc thi mới trong cơ sở dữ liệu.
     *
     * @param competition Đối tượng Competition cần tạo
     * @return true nếu tạo thành công, ngược lại false
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean createCompetition(Competition competition) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `competitions` (name, description, class_id, quiz_id, time_limit, question_count, shuffle_questions, access_start_time, access_end_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, competition.getName());
            ps.setString(2, competition.getDescription());
            ps.setInt(3, competition.getClassId());
            ps.setInt(4, competition.getQuizId());
            ps.setInt(5, competition.getTimeLimit());
            ps.setInt(6, competition.getQuestionCount());
            ps.setBoolean(7, competition.isShuffleQuestions());
            ps.setDate(8, (Date) competition.getAccessStartTime());
            ps.setDate(9, (Date) competition.getAccessEndTime());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        competition.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Lấy danh sách các cuộc thi của một giáo viên cụ thể.
     *
     * @param teacherId ID của giáo viên
     * @return Danh sách các cuộc thi
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<Competition> getCompetitionsByTeacher(int teacherId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT c.* FROM `competitions` c JOIN `class` cl ON c.class_id = cl.id WHERE cl.teacher_id = ?";
        List<Competition> competitions = new ArrayList<>();

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Competition competition = new Competition();
                    competition.setId(rs.getInt("id"));
                    competition.setName(rs.getString("name"));
                    competition.setDescription(rs.getString("description"));
                    competition.setClassId(rs.getInt("class_id"));
                    competition.setQuizId(rs.getInt("quiz_id"));
                    competition.setTimeLimit(rs.getInt("time_limit"));
                    competition.setQuestionCount(rs.getInt("question_count"));
                    competition.setShuffleQuestions(rs.getBoolean("shuffle_questions"));
                    competition.setAccessStartTime(rs.getDate("access_start_time"));
                    competition.setAccessEndTime(rs.getDate("access_end_time"));
                    competition.setCreatedAt(rs.getTimestamp("created_at"));

                    competitions.add(competition);
                }
            }
        }

        return competitions;
    }

    /**
     * Lấy một cuộc thi theo ID.
     *
     * @param competitionId ID của cuộc thi
     * @return Đối tượng Competition nếu tồn tại, ngược lại trả về null
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Competition getCompetitionById(int competitionId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `competitions` WHERE id = ?";
        Competition competition = null;

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, competitionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    competition = new Competition();
                    competition.setId(rs.getInt("id"));
                    competition.setName(rs.getString("name"));
                    competition.setDescription(rs.getString("description"));
                    competition.setClassId(rs.getInt("class_id"));
                    competition.setQuizId(rs.getInt("quiz_id"));
                    competition.setTimeLimit(rs.getInt("time_limit"));
                    competition.setQuestionCount(rs.getInt("question_count"));
                    competition.setShuffleQuestions(rs.getBoolean("shuffle_questions"));
                    competition.setAccessStartTime(rs.getDate("access_start_time"));
                    competition.setAccessEndTime(rs.getDate("access_end_time"));
                    competition.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        }

        return competition;
    }

    /**
     * Cập nhật thông tin của một cuộc thi.
     *
     * @param competition Đối tượng Competition cần cập nhật
     * @return true nếu cập nhật thành công, ngược lại false
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean updateCompetition(Competition competition) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE `competitions` SET name = ?, description = ?, class_id = ?, quiz_id = ?, time_limit = ?, question_count = ?, shuffle_questions = ?, access_start_time = ?, access_end_time = ? WHERE id = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, competition.getName());
            ps.setString(2, competition.getDescription());
            ps.setInt(3, competition.getClassId());
            ps.setInt(4, competition.getQuizId());
            ps.setInt(5, competition.getTimeLimit());
            ps.setInt(6, competition.getQuestionCount());
            ps.setBoolean(7, competition.isShuffleQuestions());
            ps.setDate(8, (Date) competition.getAccessStartTime());
            ps.setDate(9, (Date) competition.getAccessEndTime());
            ps.setInt(10, competition.getId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }

    /**
     * Xóa một cuộc thi theo ID.
     *
     * @param competitionId ID của cuộc thi
     * @return true nếu xóa thành công, ngược lại false
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean deleteCompetition(int competitionId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM `competitions` WHERE id = ?";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, competitionId);
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
    }
}
