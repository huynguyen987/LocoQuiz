package dao;

import entity.Competition;
import entity.Question;
import entity.classs;
import entity.quiz;
import Module.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class CompetitionDAO {

    //    getClassInfo
    public classs getClassInfo(int competitionId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT cl.* FROM `class` cl JOIN `competitions` c ON cl.id = c.class_id WHERE c.id = ?";
        classs classInfo = null;

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, competitionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    classInfo = new classs();
                    classInfo.setId(rs.getInt("id"));
                    classInfo.setName(rs.getString("name"));
                    classInfo.setTeacher_id(rs.getInt("teacher_id"));
                    classInfo.setCreated_at(rs.getTimestamp("created_at"));
                }
            }
        }

        return classInfo;
    }

    //    getQuizInfo
    public quiz getQuizInfo(int competitionId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT q.* FROM `quizzes` q JOIN `competitions` c ON q.id = c.quiz_id WHERE c.id = ?";
        quiz quizInfo = null;

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, competitionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quizInfo = new quiz();
                    quizInfo.setId(rs.getInt("id"));
                    quizInfo.setName(rs.getString("name"));
                    quizInfo.setDescription(rs.getString("description"));
                    quizInfo.setCreated_at(String.valueOf(rs.getTimestamp("created_at")));
                }
            }
        }

        return quizInfo;
    }

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

            // Chuyển đổi java.util.Date sang java.sql.Timestamp
            if (competition.getAccessStartTime() != null) {
                ps.setTimestamp(8, new Timestamp(competition.getAccessStartTime().getTime()));
            } else {
                ps.setTimestamp(8, null);
            }

            if (competition.getAccessEndTime() != null) {
                ps.setTimestamp(9, new Timestamp(competition.getAccessEndTime().getTime()));
            } else {
                ps.setTimestamp(9, null);
            }

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

            // Chuyển đổi java.util.Date sang java.sql.Timestamp
            if (competition.getAccessStartTime() != null) {
                ps.setTimestamp(8, new Timestamp(competition.getAccessStartTime().getTime()));
            } else {
                ps.setTimestamp(8, null);
            }

            if (competition.getAccessEndTime() != null) {
                ps.setTimestamp(9, new Timestamp(competition.getAccessEndTime().getTime()));
            } else {
                ps.setTimestamp(9, null);
            }

            ps.setInt(10, competition.getId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        }
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

                    Timestamp startTS = rs.getTimestamp("access_start_time");
                    Timestamp endTS = rs.getTimestamp("access_end_time");

                    competition.setAccessStartTime(startTS != null ? new Date(startTS.getTime()) : null);
                    competition.setAccessEndTime(endTS != null ? new Date(endTS.getTime()) : null);

                    competition.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }
        }

        return competition;
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

                    Timestamp startTS = rs.getTimestamp("access_start_time");
                    Timestamp endTS = rs.getTimestamp("access_end_time");

                    competition.setAccessStartTime(startTS != null ? new Date(startTS.getTime()) : null);
                    competition.setAccessEndTime(endTS != null ? new Date(endTS.getTime()) : null);

                    competition.setCreatedAt(rs.getTimestamp("created_at"));

                    competitions.add(competition);
                }
            }
        }

        return competitions;
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

    /**
     * Lấy danh sách các câu hỏi liên quan đến một cuộc thi cụ thể.
     *
     * @param competitionId ID của cuộc thi
     * @return Danh sách các câu hỏi
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<Question> getQuestionsForCompetition(int competitionId) throws SQLException, ClassNotFoundException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.* FROM `questions` q JOIN `quizzes` qu ON q.quiz_id = qu.id JOIN `competitions` c ON qu.id = c.quiz_id WHERE c.id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, competitionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question question = new Question();
                    question.setQuestion(rs.getString("question_text"));
                    question.setCorrect(rs.getString("correct_answer"));
                    // options are stored as comma-separated string in the database
                    String[] options = rs.getString("options").split(",");
                    List<String> optionsList = new ArrayList<>();
                    for (String option : options) {
                        optionsList.add(option.trim());
                    }
                    question.setOptions(optionsList);
                    // Nếu câu hỏi có trường ngày tháng nào, xử lý tương tự

                    questions.add(question);
                }
            }
        }
        return questions;
    }

    public quiz getQuizByCompetitionId(int competitionId) {
        String sql = "SELECT q.* FROM `quiz` q JOIN `competitions` c ON q.id = c.quiz_id WHERE c.id = ?";
        quiz quizInfo = null;

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, competitionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quizInfo = new quiz();
                    quizInfo.setId(rs.getInt("id"));
                    quizInfo.setName(rs.getString("name"));
                    quizInfo.setDescription(rs.getString("description"));
                    quizInfo.setCreated_at(String.valueOf(rs.getTimestamp("created_at")));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return quizInfo;
    }
}
