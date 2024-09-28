/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import entity.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.SubjectDAO;

/**
 *
 * @author hient
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    private SubjectDAO subjectDAO;

    @Override
    public void init() {
        subjectDAO = new SubjectDAO(); // Khởi tạo DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String subjectIdParam = request.getParameter("subjectId");
        if (subjectIdParam == null || subjectIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subject ID is missing");
            return;
        }

        int subjectId;
        try {
            subjectId = Integer.parseInt(subjectIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Subject ID");
            return;
        }

        Subject subject = subjectDAO.getSubjectById(subjectId);
        if (subject == null || subject.getAvatar() == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }

        byte[] imageBytes = subject.getAvatar();
        response.setContentType("image/jpeg"); // Hoặc loại hình ảnh phù hợp
        response.setContentLength(imageBytes.length);

        OutputStream os = response.getOutputStream();
        os.write(imageBytes);
        os.flush();
    }
}