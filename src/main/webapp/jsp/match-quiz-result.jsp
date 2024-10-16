<%@ page import="java.util.List" %>
<%@ page import="Servlet.SubmitMatchQuizServlet.QuestionResult" %>
<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    // Retrieve quiz result from session
    List<QuestionResult> quizResult = (List<QuestionResult>) session.getAttribute("quizResult");
    if (quizResult == null) {
        // Redirect to homepage if no quiz result is found
        response.sendRedirect(request.getContextPath() + "/jsp/student.jsp");
        return;
    }
    // Remove quiz result from session to prevent resubmission
    session.removeAttribute("quizResult");

    // Retrieve timeTaken from session
    Integer timeTakenObj = (Integer) session.getAttribute("timeTaken");
    int timeTaken = (timeTakenObj != null) ? timeTakenObj : 0;
    session.removeAttribute("timeTaken");

    // Calculate score
    int score = 0;
    for (QuestionResult qr : quizResult) {
        if (qr.isCorrect()) {
            score++;
        }
    }
    int total = quizResult.size();

    // Retrieve incorrectAnswers from session
    List<QuestionResult> incorrectAnswers = (List<QuestionResult>) session.getAttribute("incorrectAnswers");
    if (incorrectAnswers == null) {
        incorrectAnswers = new ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Match Quiz Results</title>
    <!-- Link to External CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/match-quiz-result.css">
</head>
<body>
<div class="container">
    <h1>Match Quiz Results</h1>
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
        <button onclick="window.location.href='<%= request.getContextPath() %>/jsp/student.jsp'">Home</button>
        <button onclick="window.location.href='<%= request.getContextPath() %>/AllQuizzesServlet'">Take Another Quiz</button>
    </div>
</div>
</body>
</html>
