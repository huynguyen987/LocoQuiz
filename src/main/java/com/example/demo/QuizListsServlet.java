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
import java.util.List;

/**
 *
 * @author hient
 */
@WebServlet("/QuizListsServlet")
public class QuizListsServlet extends HttpServlet {
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

        // Lấy danh sách các môn học từ DAO
        List<Subject> subjectList = subjectDAO.getAllSubjects();

        // Đặt thuộc tính cho request
        request.setAttribute("subjectList", subjectList);

        // Chuyển tiếp đến JSP hiển thị danh sách môn học
        request.getRequestDispatcher("/jsp/quiz-lists.jsp").forward(request, response);
    }
}
