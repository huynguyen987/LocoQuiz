package dao;

import entity.Flashcard;
import Module.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDAO {

    // Method to get all flashcards
    public List<Flashcard> getAllFlashcards() throws SQLException, ClassNotFoundException {
        List<Flashcard> flashcards = new ArrayList<>();
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM flashcard";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Flashcard flashcard = extractFlashcardFromResultSet(rs);
                flashcards.add(flashcard);
            }
        }
        return flashcards;
    }

    // Method to get flashcard by ID
    public Flashcard getFlashcardById(int id) throws SQLException, ClassNotFoundException {
        Flashcard flashcard = null;
        Connection connection = new DBConnect().getConnection();
        String sql = "SELECT * FROM flashcard WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flashcard = extractFlashcardFromResultSet(rs);
            }
        }
        return flashcard;
    }

    // Insert flashcard and return generated ID
    public int insertFlashcard(Flashcard flashcard) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "INSERT INTO flashcard(front_text, back_text, category) VALUES(?, ?, ?)";
        int generatedId = -1;
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, flashcard.getFrontText());
            ps.setString(2, flashcard.getBackText());
            ps.setString(3, flashcard.getCategory());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
        }
        return generatedId;
    }

    // Update flashcard
    public boolean updateFlashcard(Flashcard flashcard) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "UPDATE flashcard SET front_text = ?, back_text = ?, category = ?, correct_answers = ?, incorrect_answers = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, flashcard.getFrontText());
            ps.setString(2, flashcard.getBackText());
            ps.setString(3, flashcard.getCategory());
            ps.setInt(4, flashcard.getCorrectAnswers());
            ps.setInt(5, flashcard.getIncorrectAnswers());
            ps.setInt(6, flashcard.getId());
            return ps.executeUpdate() == 1;
        }
    }

    // Delete flashcard by ID
    public boolean deleteFlashcard(int id) throws SQLException, ClassNotFoundException {
        Connection connection = new DBConnect().getConnection();
        String sql = "DELETE FROM flashcard WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Helper method to extract flashcard from ResultSet
    private Flashcard extractFlashcardFromResultSet(ResultSet rs) throws SQLException {
        Flashcard flashcard = new Flashcard();
        flashcard.setId(rs.getInt("id"));
        flashcard.setFrontText(rs.getString("front_text"));
        flashcard.setBackText(rs.getString("back_text"));
        flashcard.setCategory(rs.getString("category"));
        flashcard.setCreatedAt(rs.getTimestamp("created_at").toString());
        flashcard.setUpdatedAt(rs.getTimestamp("updated_at").toString());
        flashcard.setCorrectAnswers(rs.getInt("correct_answers"));
        flashcard.setIncorrectAnswers(rs.getInt("incorrect_answers"));
        return flashcard;
    }
}
