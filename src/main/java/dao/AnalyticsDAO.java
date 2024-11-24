package dao;

import Module.DBConnect;
import entity.class_quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class AnalyticsDAO {
    private DBConnect db;

    public AnalyticsDAO() {
        db = new DBConnect();
    }

    // Lấy tổng số quiz do giáo viên tạo
    public int getTotalQuizzesByTeacher(int teacherId) {
        int total = 0;
        String sql = "SELECT COUNT(*) AS total_quizzes FROM quiz WHERE user_id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total_quizzes");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return total;
    }

    // Lấy tổng số lần làm quiz từ bảng result và competition_results
    public int getTotalQuizAttemptsByTeacher(int teacherId) {
        int total = 0;
        String sqlResult = "SELECT COUNT(*) AS total_attempts FROM result r JOIN quiz q ON r.quiz_id = q.id WHERE q.user_id = ?";
        String sqlCompetition = "SELECT COUNT(*) AS total_competition_attempts FROM competition_results cr " +
                "JOIN competitions c ON cr.competition_id = c.id " +
                "JOIN quiz q ON c.quiz_id = q.id " +
                "WHERE q.user_id = ?";

        try (Connection conn = db.getConnection()) {
            // Truy vấn từ bảng result
            try (PreparedStatement ps = conn.prepareStatement(sqlResult)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total += rs.getInt("total_attempts");
                    }
                }
            }

            // Truy vấn từ bảng competition_results
            try (PreparedStatement ps = conn.prepareStatement(sqlCompetition)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        total += rs.getInt("total_competition_attempts");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return total;
    }

    // Lấy điểm trung bình từ bảng result và competition_results
    public double getAverageScoreByTeacher(int teacherId) {
        double totalScore = 0.0;
        int count = 0;

        String sqlResult = "SELECT AVG(r.score) AS avg_score FROM result r JOIN quiz q ON r.quiz_id = q.id WHERE q.user_id = ?";
        String sqlCompetition = "SELECT AVG(cr.score) AS avg_competition_score FROM competition_results cr " +
                "JOIN competitions c ON cr.competition_id = c.id " +
                "JOIN quiz q ON c.quiz_id = q.id " +
                "WHERE q.user_id = ?";

        try (Connection conn = db.getConnection()) {
            // Truy vấn từ bảng result
            try (PreparedStatement ps = conn.prepareStatement(sqlResult)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double avg = rs.getDouble("avg_score");
                        if (!rs.wasNull()) {
                            totalScore += avg;
                            count++;
                        }
                    }
                }
            }

            // Truy vấn từ bảng competition_results
            try (PreparedStatement ps = conn.prepareStatement(sqlCompetition)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double avg = rs.getDouble("avg_competition_score");
                        if (!rs.wasNull()) {
                            totalScore += avg;
                            count++;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count > 0 ? totalScore / count : 0.0;
    }

    // Lấy thời gian làm bài trung bình từ bảng competition_results
    public double getAverageTimeByTeacher(int teacherId) {
        double totalTime = 0.0;
        int count = 0;

        String sqlCompetition = "SELECT AVG(cr.timeTaken) AS avg_time FROM competition_results cr " +
                "JOIN competitions c ON cr.competition_id = c.id " +
                "JOIN quiz q ON c.quiz_id = q.id " +
                "WHERE q.user_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlCompetition)) {

            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble("avg_time");
                    if (!rs.wasNull()) {
                        totalTime += avg;
                        count++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return count > 0 ? totalTime / count : 0.0;
    }

    // Lấy dữ liệu điểm số theo thời gian từ bảng result và competition_results
    public Map<String, Double> getScoreTrendsByTeacher(int teacherId) {
        Map<String, Double> trends = new LinkedHashMap<>();

        String sqlResult = "SELECT DATE(r.created_at) AS date, AVG(r.score) AS average_score " +
                "FROM result r JOIN quiz q ON r.quiz_id = q.id " +
                "WHERE q.user_id = ? " +
                "GROUP BY DATE(r.created_at) " +
                "ORDER BY DATE(r.created_at)";
        String sqlCompetition = "SELECT DATE(cr.created_at) AS date, AVG(cr.score) AS average_competition_score " +
                "FROM competition_results cr JOIN competitions c ON cr.competition_id = c.id " +
                "JOIN quiz q ON c.quiz_id = q.id " +
                "WHERE q.user_id = ? " +
                "GROUP BY DATE(cr.created_at) " +
                "ORDER BY DATE(cr.created_at)";

        try (Connection conn = db.getConnection()) {
            // Truy vấn từ bảng result
            try (PreparedStatement ps = conn.prepareStatement(sqlResult)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String date = rs.getString("date");
                        double avgScore = rs.getDouble("average_score");
                        if (!rs.wasNull()) {
                            trends.put(date, avgScore);
                        }
                    }
                }
            }

            // Truy vấn từ bảng competition_results
            try (PreparedStatement ps = conn.prepareStatement(sqlCompetition)) {
                ps.setInt(1, teacherId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String date = rs.getString("date");
                        double avgScore = rs.getDouble("average_competition_score");
                        if (!rs.wasNull()) {
                            if (trends.containsKey(date)) {
                                // Trung bình tổng hợp nếu cùng ngày
                                double existing = trends.get(date);
                                trends.put(date, (existing + avgScore) / 2);
                            } else {
                                trends.put(date, avgScore);
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return trends;
    }

    // Thêm phương thức DAO mới: Lấy tất cả các class_quiz
    public List<class_quiz> getAllClassQuiz() {
        List<class_quiz> classQuizzes = new ArrayList<>();
        String sql = "SELECT * FROM class_quiz";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                class_quiz cq = new class_quiz();
                cq.setClass_id(rs.getInt("class_id"));
                cq.setQuiz_id(rs.getInt("quiz_id"));
                classQuizzes.add(cq);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return classQuizzes;
    }




}
