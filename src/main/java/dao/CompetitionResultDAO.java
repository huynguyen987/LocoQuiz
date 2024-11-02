package dao;

import entity.CompetitionResult;
import Module.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetitionResultDAO {

    // Lấy tất cả kết quả
    public List<CompetitionResult> getAllCompetitionResults() throws SQLException, ClassNotFoundException {
        List<CompetitionResult> competitionResults = new ArrayList<>();
        String sql = "SELECT * FROM competition_results";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CompetitionResult cr = new CompetitionResult();
                cr.setId(rs.getInt("id"));
                cr.setCompetitionId(rs.getInt("competition_id"));
                cr.setUserId(rs.getInt("user_id"));
                cr.setClassId(rs.getInt("class_id"));
                cr.setScore(rs.getFloat("score"));
                cr.setCreatedAt(rs.getTimestamp("created_at"));

                competitionResults.add(cr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitionResults;
    }

    // Lấy kết quả theo ID
    public CompetitionResult getCompetitionResultById(int id) throws SQLException, ClassNotFoundException {
        CompetitionResult cr = null;
        String sql = "SELECT * FROM competition_results WHERE id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cr = new CompetitionResult();
                    cr.setId(rs.getInt("id"));
                    cr.setCompetitionId(rs.getInt("competition_id"));
                    cr.setUserId(rs.getInt("user_id"));
                    cr.setClassId(rs.getInt("class_id"));
                    cr.setScore(rs.getFloat("score"));
                    cr.setCreatedAt(rs.getTimestamp("created_at"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cr;
    }

    // Thêm kết quả mới
    public boolean insertCompetitionResult(CompetitionResult cr) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO competition_results (competition_id, user_id, class_id, score, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cr.getCompetitionId());
            ps.setInt(2, cr.getUserId());
            ps.setInt(3, cr.getClassId());
            ps.setFloat(4, cr.getScore());
            ps.setTimestamp(5, cr.getCreatedAt());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật kết quả
    public boolean updateCompetitionResult(CompetitionResult cr) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE competition_results SET competition_id = ?, user_id = ?, class_id = ?, score = ?, created_at = ? WHERE id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cr.getCompetitionId());
            ps.setInt(2, cr.getUserId());
            ps.setInt(3, cr.getClassId());
            ps.setFloat(4, cr.getScore());
            ps.setTimestamp(5, cr.getCreatedAt());
            ps.setInt(6, cr.getId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xóa kết quả
    public boolean deleteCompetitionResult(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM competition_results WHERE id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy kết quả theo competition_id
    public List<CompetitionResult> getCompetitionResultsByCompetitionId(int competitionId) throws SQLException, ClassNotFoundException {
        List<CompetitionResult> competitionResults = new ArrayList<>();
        String sql = "SELECT * FROM competition_results WHERE competition_id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, competitionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompetitionResult cr = new CompetitionResult();
                    cr.setId(rs.getInt("id"));
                    cr.setCompetitionId(rs.getInt("competition_id"));
                    cr.setUserId(rs.getInt("user_id"));
                    cr.setClassId(rs.getInt("class_id"));
                    cr.setScore(rs.getFloat("score"));
                    cr.setCreatedAt(rs.getTimestamp("created_at"));

                    competitionResults.add(cr);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitionResults;
    }

    // Lấy kết quả theo user_id
    public List<CompetitionResult> getCompetitionResultsByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<CompetitionResult> competitionResults = new ArrayList<>();
        String sql = "SELECT * FROM competition_results WHERE user_id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompetitionResult cr = new CompetitionResult();
                    cr.setId(rs.getInt("id"));
                    cr.setCompetitionId(rs.getInt("competition_id"));
                    cr.setUserId(rs.getInt("user_id"));
                    cr.setClassId(rs.getInt("class_id"));
                    cr.setScore(rs.getFloat("score"));
                    cr.setCreatedAt(rs.getTimestamp("created_at"));

                    competitionResults.add(cr);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitionResults;
    }

    // Lấy kết quả theo class_id
    public List<CompetitionResult> getCompetitionResultsByClassId(int classId) throws SQLException, ClassNotFoundException {
        List<CompetitionResult> competitionResults = new ArrayList<>();
        String sql = "SELECT * FROM competition_results WHERE class_id = ?";

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompetitionResult cr = new CompetitionResult();
                    cr.setId(rs.getInt("id"));
                    cr.setCompetitionId(rs.getInt("competition_id"));
                    cr.setUserId(rs.getInt("user_id"));
                    cr.setClassId(rs.getInt("class_id"));
                    cr.setScore(rs.getFloat("score"));
                    cr.setCreatedAt(rs.getTimestamp("created_at"));

                    competitionResults.add(cr);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return competitionResults;
    }

    // Phương thức main để kiểm tra
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        CompetitionResultDAO dao = new CompetitionResultDAO();
        List<CompetitionResult> competitionResults = dao.getAllCompetitionResults();
        for (CompetitionResult cr : competitionResults) {
            System.out.println(cr.toString());
        }
    }
}
