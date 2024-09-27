package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Results;
import Module.*;

public class ResultsDAO {

    //get all results
    public static ArrayList<Results> getAllResults() throws SQLException, ClassNotFoundException {
        ArrayList<Results> results = new ArrayList<Results>();
        Connection connection = new DBConnect().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM results");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Results result = new Results(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("quiz_id"), rs.getInt("class_id"), rs.getDouble("score"), rs.getString("created_at"));
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //get results by user_id
    public static ArrayList<Results> getResultsByUserId(int user_id) throws SQLException, ClassNotFoundException {
        ArrayList<Results> results = new ArrayList<Results>();
        Connection connection = new DBConnect().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM results WHERE user_id = ?");
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Results result = new Results(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("quiz_id"), rs.getInt("class_id"), rs.getDouble("score"), rs.getString("created_at"));
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    //update results
    public static void updateResults(Results result) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE results SET user_id = ?, quiz_id = ?, class_id = ?, score = ?, created_at = ? WHERE id = ?");
            stmt.setInt(1, result.getUser_id());
            stmt.setInt(2, result.getQuiz_id());
            stmt.setInt(3, result.getClass_id());
            stmt.setDouble(4, result.getScore());
            stmt.setString(5, result.getCreated_at());
            stmt.setInt(6, result.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //delete results
    public static void deleteResults(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM results WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //add results
    public static void addResults(Results result) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO results (user_id, quiz_id, class_id, score, created_at) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, result.getUser_id());
            stmt.setInt(2, result.getQuiz_id());
            stmt.setInt(3, result.getClass_id());
            stmt.setDouble(4, result.getScore());
            stmt.setString(5, result.getCreated_at());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //main method
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //test getAllResults
        ArrayList<Results> results = getAllResults();
        for (Results result : results) {
            System.out.println(result);
        }

        //test getResultsByUserId
        ArrayList<Results> resultsByUserId = getResultsByUserId(1);
        for (Results result : resultsByUserId) {
            System.out.println(result);
        }

        //test updateResults
        Results result = new Results(1, 1, 1, 1, 10, "2021-01-01");
        updateResults(result);

        //test deleteResults
        deleteResults(1);

        //test addResults
        Results result1 = new Results(1, 1, 1, 1, 10, "2021-01-01");
        addResults(result1);
    }
}
