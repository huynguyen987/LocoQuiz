package dao;

import entity.Users;
import Module.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Module.*;

public class UsersDAO {

    // Utility method to extract user data from ResultSet
    private static Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password")); // Nên mã hóa mật khẩu trước khi hiển thị hoặc sử dụng
        user.setEmail(rs.getString("email"));
        user.setRole_id(rs.getInt("role_id"));
        user.setCreated_at(rs.getString("created_at"));
        user.setAvatar(rs.getBytes("avatar"));
        user.setFeature_face(rs.getBytes("feature_face"));
        user.setGender(rs.getString("gender"));
        return user;
    }

    // Get all Users
    public List<Users> getAllUsers() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users";
        List<Users> users = new ArrayList<>();

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users user = extractUserFromResultSet(rs);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Có thể throw lại hoặc xử lý theo cách khác tùy vào yêu cầu
        }

        return users;
    }

    // Get user by ID
    public static Users getUserById(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Get user by Email
    public Users getUserByEmail(String email) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE email = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Get user by Username
    public Users getUserByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // Add new user
    public boolean addUser(Users user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO users(username, password, email, role_id, created_at, avatar, feature_face, gender) VALUES(?,?,?,?,?,?,?,?)";
        boolean isAdded = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRole_id());
            ps.setString(5, user.getCreated_at());
            ps.setBytes(6, user.getAvatar());
            ps.setBytes(7, user.getFeature_face());
            ps.setString(8, user.getGender());

            int affectedRows = ps.executeUpdate();
            isAdded = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // Update user
    public boolean updateUser(Users user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, role_id = ?, avatar = ?, feature_face = ?, gender = ? WHERE id = ?";
        boolean isUpdated = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); // Nên mã hóa mật khẩu trước khi lưu
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRole_id());
            ps.setBytes(5, user.getAvatar());
            ps.setBytes(6, user.getFeature_face());
            ps.setString(7, user.getGender());
            ps.setInt(8, user.getId());

            int affectedRows = ps.executeUpdate();
            isUpdated = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // Delete user and related data
    public boolean deleteUser(int id) throws SQLException, ClassNotFoundException {
        // Define SQL delete statements for all related tables
        String deleteCompetitionResults = "DELETE FROM competition_results WHERE user_id = ?";
        String deleteCompetitions = "DELETE FROM competitions WHERE quiz_id IN (SELECT id FROM quiz WHERE user_id = ?)";
        String deleteQuizTags = "DELETE FROM quiz_tag WHERE quiz_id IN (SELECT id FROM quiz WHERE user_id = ?)";
        String deleteUserLibrary = "DELETE FROM user_library WHERE user_id = ?";
        String deleteResults = "DELETE FROM result WHERE user_id = ?";
        String deleteUserQuiz = "DELETE FROM user_quiz WHERE user_id = ?";
        String deleteClassQuizzes = "DELETE FROM class_quiz WHERE class_id IN (SELECT id FROM class WHERE teacher_id = ?)";
        String deleteClassUser = "DELETE FROM class_user WHERE user_id = ?";
        String deleteClasses = "DELETE FROM class WHERE teacher_id = ?";
        String deleteQuiz = "DELETE FROM quiz WHERE user_id = ?";
        String deleteUser = "DELETE FROM users WHERE id = ?";
        boolean isDeleted = false;

        try (Connection connection = new DBConnect().getConnection()) {
            // Start transaction
            connection.setAutoCommit(false);

            try (
                    PreparedStatement psCompetitionResults = connection.prepareStatement(deleteCompetitionResults);
                    PreparedStatement psCompetitions = connection.prepareStatement(deleteCompetitions);
                    PreparedStatement psQuizTags = connection.prepareStatement(deleteQuizTags);
                    PreparedStatement psUserLibrary = connection.prepareStatement(deleteUserLibrary);
                    PreparedStatement psResults = connection.prepareStatement(deleteResults);
                    PreparedStatement psUserQuiz = connection.prepareStatement(deleteUserQuiz);
                    PreparedStatement psClassQuizzes = connection.prepareStatement(deleteClassQuizzes);
                    PreparedStatement psClassUser = connection.prepareStatement(deleteClassUser);
                    PreparedStatement psClasses = connection.prepareStatement(deleteClasses);
                    PreparedStatement psQuiz = connection.prepareStatement(deleteQuiz);
                    PreparedStatement psUser = connection.prepareStatement(deleteUser)
            ) {

                // Set user ID for all queries
                psCompetitionResults.setInt(1, id);
                psCompetitions.setInt(1, id);
                psQuizTags.setInt(1, id);
                psUserLibrary.setInt(1, id);
                psResults.setInt(1, id);
                psUserQuiz.setInt(1, id);
                psClassQuizzes.setInt(1, id);
                psClassUser.setInt(1, id);
                psClasses.setInt(1, id);
                psQuiz.setInt(1, id);
                psUser.setInt(1, id);

                // Execute delete statements in the correct order
                // 1. Delete competition_results related to the user
                psCompetitionResults.executeUpdate();

                // 2. Delete competitions related to the user's quizzes
                psCompetitions.executeUpdate();

                // 3. Delete quiz_tags related to the user's quizzes
                psQuizTags.executeUpdate();

                // 4. Delete user_library entries related to the user
                psUserLibrary.executeUpdate();

                // 5. Delete results related to the user
                psResults.executeUpdate();

                // 6. Delete user_quiz related to the user
                psUserQuiz.executeUpdate();

                // 7. Delete class_quiz related to classes taught by the user
                psClassQuizzes.executeUpdate();

                // 8. Delete class_user records related to the user
                psClassUser.executeUpdate();

                // 9. Delete classes taught by the user
                psClasses.executeUpdate();

                // 10. Delete quizzes created by the user
                psQuiz.executeUpdate();

                // 11. Finally, delete the user
                int affectedRows = psUser.executeUpdate();

                // Commit transaction if user deletion succeeded
                isDeleted = affectedRows > 0;
                connection.commit();

            } catch (SQLException e) {
                // Rollback transaction in case of error
                connection.rollback();
                e.printStackTrace();
            } finally {
                // Restore default commit behavior
                connection.setAutoCommit(true);
            }
        }

        return isDeleted;
    }


    // Get all students not in class
    public List<Users> getAllStudentsNotInClass(int classId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE role_id = 2 AND id NOT IN (SELECT user_id FROM class_user WHERE class_id = ?)";
        List<Users> students = new ArrayList<>();
        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users student = new Users();
                    student.setId(rs.getInt("id"));
                    student.setUsername(rs.getString("username"));
                    student.setEmail(rs.getString("email"));
                    students.add(student);
                }
            }
        }
        return students;
    }

    //check login
    public Users checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Users user = null;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    // Hash the entered password for comparison
                    String hashedPassword = HashPassword.hashPassword(password);

                    // Compare hashed passwords
                    if (storedHashedPassword.equals(hashedPassword)) {
                        // Authentication successful
                        user = new Users();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        user.setRole_id(rs.getInt("role_id"));
                    }
                }
            }
        }

        return user; // Returns null if authentication fails
    }

    public boolean updateUserForUploadImage(Users user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE users SET username = ?, email = ?, gender = ?";

        // Kiểm tra nếu avatar không null, thêm vào câu lệnh SQL
        if (user.getAvatar() != null) {
            sql += ", avatar = ?";
        }

        sql += " WHERE id = ?";

        boolean isUpdated = false;

        try (Connection connection = new DBConnect().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int paramIndex = 1;
            ps.setString(paramIndex++, user.getUsername());
            ps.setString(paramIndex++, user.getEmail());
            ps.setString(paramIndex++, user.getGender());

            if (user.getAvatar() != null) {
                ps.setBytes(paramIndex++, user.getAvatar());
            }

            ps.setInt(paramIndex, user.getId());

            int affectedRows = ps.executeUpdate();
            isUpdated = affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để Servlet có thể xử lý
        }

        return isUpdated;
    }
}

