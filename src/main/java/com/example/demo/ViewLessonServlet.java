/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.demo;

import entity.Lessons;
import model.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hient
 */
@WebServlet("/viewLesson")
public class ViewLessonServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kết nối cơ sở dữ liệu và lấy danh sách bài học
        List<Lessons> lessonsList = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM lessons"; // Lệnh SQL lấy tất cả bài học
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Duyệt qua các bản ghi và thêm vào danh sách bài học
            while (rs.next()) {
                Lessons lesson = new Lessons();
                lesson.setId(rs.getInt("id"));
                lesson.setTitle(rs.getString("title"));
                lesson.setContent(rs.getString("content"));
                lessonsList.add(lesson);
            }

            // Đóng kết nối
            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(); // Ghi lỗi ra console, có thể thêm log để ghi log lỗi
        }

        // Đặt danh sách bài học trong request để hiển thị trong view
        request.setAttribute("lessons", lessonsList);
        request.getRequestDispatcher("/view-lessons.jsp").forward(request, response);
    }
}
