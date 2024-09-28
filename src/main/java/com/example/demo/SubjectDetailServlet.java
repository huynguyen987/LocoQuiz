/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;

import entity.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.*;
import model.SubjectDAO;

import java.io.IOException;

/**
 *
 * @author hient
 */
@WebServlet("/SubjectDetailServlet")
public class SubjectDetailServlet extends HttpServlet {
    private SubjectDAO subjectDAO;

    @Override
    public void init() {
        subjectDAO = new SubjectDAO(); // Khởi tạo DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Kiểm tra người dùng đã đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            // Nếu chưa đăng nhập, chuyển hướng đến trang đăng nhập
            response.sendRedirect("jsp/login.jsp");
            return;
        }

        // Lấy subjectId từ request
        String subjectIdParam = request.getParameter("subjectId");
        if (subjectIdParam == null || subjectIdParam.isEmpty()) {
            // Nếu không có subjectId, chuyển hướng về danh sách môn học
            response.sendRedirect("QuizListsServlet");
            return;
        }

        int subjectId;
        try {
            subjectId = Integer.parseInt(subjectIdParam);
        } catch (NumberFormatException e) {
            // Nếu subjectId không hợp lệ, chuyển hướng về danh sách môn học
            response.sendRedirect("QuizListsServlet");
            return;
        }

        // Lấy thông tin môn học và các bài học từ cơ sở dữ liệu
        Subject subject = subjectDAO.getSubjectById(subjectId);
        if (subject == null) {
            // Nếu không tìm thấy môn học, chuyển hướng về danh sách môn học
            response.sendRedirect("QuizListsServlet");
            return;
        }

        // Đặt thuộc tính cho request
        request.setAttribute("subject", subject);

        // Chuyển tiếp đến JSP hiển thị chi tiết môn học
        request.getRequestDispatcher("/jsp/subject-details.jsp").forward(request, response);
    }
}