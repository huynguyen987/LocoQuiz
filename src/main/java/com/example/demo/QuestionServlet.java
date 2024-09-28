/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import entity.Questions;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.QuestionsDAO;

/**
 *
 * @author hient
 */
@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
    private QuestionsDAO questionDAO;

    @Override
    public void init() {
        questionDAO = new QuestionsDAO(); // Khởi tạo DAO
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quizIdParam = request.getParameter("quizId");
        if (quizIdParam == null || quizIdParam.isEmpty()) {
            response.sendRedirect("quiz-lists.jsp");
            return;
        }

        int quizId;
        try {
            quizId = Integer.parseInt(quizIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("quiz-lists.jsp");
            return;
        }

        List<Questions> questionList = questionDAO.getQuestionsByQuizId(quizId);
        request.setAttribute("questionList", questionList);
        request.setAttribute("quizId", quizId);

        // Chuyển tiếp đến JSP hiển thị câu hỏi
        request.getRequestDispatcher("/jsp/display-questions.jsp").forward(request, response);
    }
}