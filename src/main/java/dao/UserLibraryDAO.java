package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entity.quiz;
import Module.DBConnect;

public class UserLibraryDAO {

    // Kiểm tra quiz có trong thư viện của người dùng hay không
    public boolean isQuizInLibrary(int userId, int quizId) {
        String sql = "SELECT 1 FROM user_library WHERE user_id = ? AND quiz_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Trả về true nếu tìm thấy bản ghi
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Thêm quiz vào thư viện của người dùng
    public boolean addQuizToLibrary(int userId, int quizId) {
        if (isQuizInLibrary(userId, quizId)) {
            System.out.println("Quiz đã có trong thư viện.");
            return false;
        }
        String sql = "INSERT INTO user_library (user_id, quiz_id) VALUES (?, ?)";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.executeUpdate();
            System.out.println("Thêm quiz vào thư viện thành công.");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa quiz khỏi thư viện của người dùng
    public boolean removeQuizFromLibrary(int userId, int quizId) {
        if (!isQuizInLibrary(userId, quizId)) {
            System.out.println("Quiz không có trong thư viện.");
            return false;
        }
        String sql = "DELETE FROM user_library WHERE user_id = ? AND quiz_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.executeUpdate();
            System.out.println("Xóa quiz khỏi thư viện thành công.");
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả quiz trong thư viện của người dùng
    public List<quiz> getUserLibrary(int userId) {
        List<quiz> quizList = new ArrayList<>();
        String sql = "SELECT q.*, ul.added_at FROM quiz q JOIN user_library ul ON q.id = ul.quiz_id WHERE ul.user_id = ?";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                QuizDAO quizDAO = new QuizDAO();
                while (rs.next()) {
                    quiz quizItem = quizDAO.extractQuizFromResultSet(rs);
                    quizItem.setAddedAt(rs.getTimestamp("added_at"));
                    quizList.add(quizItem);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return quizList;
    }
}
