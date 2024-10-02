package com.example.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.DatabaseConnection;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("q");
        List<SearchResult> results = performSearch(query);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new Gson().toJson(results);
        response.getWriter().write(json);
    }

    private List<SearchResult> performSearch(String query) {
        List<SearchResult> results = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT id, name, description FROM quiz WHERE name LIKE ? OR description LIKE ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                results.add(new SearchResult(id, name, description));
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Log the exception
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    private static class SearchResult {
        int id;
        String name;
        String description;

        SearchResult(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }
}
