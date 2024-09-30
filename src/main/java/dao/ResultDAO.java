package dao;

import entity.results;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.DBConnect;

public class ResultDAO {

    //get all result
    public List<results> getAllResult() throws SQLException, ClassNotFoundException {
        List<results> results = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM results";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results r = new results();
                r.setId(rs.getInt("id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setQuiz_id(rs.getInt("quiz_id"));
                r.setClass_id(rs.getInt("class_id"));
                r.setScore(rs.getFloat("score"));
                r.setCreated_at(rs.getTimestamp("created_at").toString());
                results.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //get result by id
    public results getResultById(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM results WHERE id = ?";
        results r = new results();
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                r.setId(rs.getInt("id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setQuiz_id(rs.getInt("quiz_id"));
                r.setClass_id(rs.getInt("class_id"));
                r.setScore(rs.getFloat("score"));
                r.setCreated_at(rs.getTimestamp("created_at").toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

    //insert result
    public boolean insertResult(results r) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO results(user_id, quiz_id, class_id, score, created_at) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, r.getUser_id());
            ps.setInt(2, r.getQuiz_id());
            ps.setInt(3, r.getClass_id());
            ps.setFloat(4, r.getScore());
            ps.setString(5, r.getCreated_at());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //update result
    public boolean updateResult(results r) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE results SET user_id = ?, quiz_id = ?, class_id = ?, score = ?, created_at = ? WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, r.getUser_id());
            ps.setInt(2, r.getQuiz_id());
            ps.setInt(3, r.getClass_id());
            ps.setFloat(4, r.getScore());
            ps.setString(5, r.getCreated_at());
            ps.setInt(6, r.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //delete result
    public boolean deleteResult(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM results WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //get result by user_id
    public List<results> getResultByUserId(int user_id) throws SQLException, ClassNotFoundException {
        List<results> results = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM results WHERE user_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results r = new results();
                r.setId(rs.getInt("id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setQuiz_id(rs.getInt("quiz_id"));
                r.setClass_id(rs.getInt("class_id"));
                r.setScore(rs.getFloat("score"));
                r.setCreated_at(rs.getTimestamp("created_at").toString());
                results.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //get result by quiz_id
    public List<results> getResultByQuizId(int quiz_id) throws SQLException, ClassNotFoundException {
        List<results> results = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM results WHERE quiz_id = ?";
        try {
            PreparedStatement ps = connection.prepareCall(sql);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results r = new results();
                r.setId(rs.getInt("id"));
                r.setUser_id(rs.getInt("user_id"));
                r.setQuiz_id(rs.getInt("quiz_id"));
                r.setClass_id(rs.getInt("class_id"));
                r.setScore(rs.getFloat("score"));
                r.setCreated_at(rs.getTimestamp("created_at").toString());
                results.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //main method for loop
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ResultDAO dao = new ResultDAO();
        List<results> results = dao.getAllResult();
        for (results r : results) {
            System.out.println(r.toString());
        }
    }
}
