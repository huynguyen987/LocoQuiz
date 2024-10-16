<%@ page import="java.util.List" %>
<%@ page import="Servlet.SubmitQuizServlet.QuestionResult" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Lấy kết quả từ session
    List<QuestionResult> quizResult = (List<QuestionResult>) session.getAttribute("quizResult");
    if (quizResult == null) {
        // Nếu không có kết quả, chuyển hướng về trang chủ
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    // Xóa kết quả khỏi session để tránh truy cập lại
    session.removeAttribute("quizResult");

    // Lấy thời gian làm bài
    Integer timeTakenObj = (Integer) session.getAttribute("timeTaken");
    int timeTaken = (timeTakenObj != null) ? timeTakenObj : 0;
    session.removeAttribute("timeTaken");

    // Tính điểm
    int score = 0;
    for (QuestionResult qr : quizResult) {
        if (qr.isCorrect()) {
            score++;
        }
    }
    int total = quizResult.size();

    // Lấy danh sách câu trả lời sai
    List<QuestionResult> incorrectAnswers = (List<QuestionResult>) session.getAttribute("incorrectAnswers");
    if (incorrectAnswers == null) {
        incorrectAnswers = new ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Results</title>
    <!-- Liên kết tới file CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz-result.css">
    <!-- Font Awesome cho biểu tượng -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<div class="container">
    <h1>Quiz Results</h1>
    <div class="summary">
        <p><strong>Score:</strong> <%= score %> out of <%= total %> (<%= String.format("%.2f", ((double)score / total) * 100) %>%)</p>
        <p><strong>Time Taken:</strong> <%= String.format("%d minutes %d seconds", timeTaken / 60, timeTaken % 60) %></p>
    </div>

    <% if (incorrectAnswers.size() > 0) { %>
    <p>You answered <strong><%= incorrectAnswers.size() %></strong> out of <strong><%= total %></strong> questions incorrectly.</p>
    <table>
        <tr>
            <th>Question</th>
            <th>Your Answer</th>
            <th>Correct Answer</th>
            <th>Result</th>
        </tr>
        <% for (QuestionResult qr : incorrectAnswers) { %>
        <tr class="<%= qr.isCorrect() ? "correct" : "incorrect" %>">
            <td><%= qr.getQuestionText() %></td>
            <td><%= (qr.getUserAnswer() != null && !qr.getUserAnswer().isEmpty()) ? qr.getUserAnswer() : "No Answer" %></td>
            <td><%= qr.getCorrectAnswer() %></td>
            <td><%= qr.isCorrect() ? "Correct" : "Incorrect" %></td>
        </tr>
        <% } %>
    </table>
    <% } else { %>
    <p>Congratulations! You answered all questions correctly.</p>
    <% } %>

    <div class="button-container">
        <button onclick="window.location.href='<%= request.getContextPath() %>/jsp/student.jsp'">
            <i class="fas fa-home"></i> Home
        </button>
        <button onclick="window.location.href='<%= request.getContextPath() %>/AllQuizzesServlet'">
            <i class="fas fa-redo"></i> Take Another Quiz
        </button>
    </div>
</div>
</body>
</html>
