<%@ page import="java.util.List" %>
<%
    String scoreParam = request.getParameter("score");
    String totalParam = request.getParameter("total");
    String quizId = request.getParameter("id");

    if (scoreParam == null || totalParam == null) {
        out.println("Error: Score or total parameter is missing.");
        return;
    }

    int score = Integer.parseInt(scoreParam);
    int total = Integer.parseInt(totalParam);

    List<String> userAnswers = (List<String>) session.getAttribute("userAnswers");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Result</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/quiz.css">
</head>
<body>
<div class="result-container">
    <h1>Your Quiz Result</h1>
    <p>You scored <strong><%= score %></strong> out of <strong><%= total %></strong>.</p>

    <!-- Optionally display user answers -->
    <% if (userAnswers != null) { %>
    <h2>Your Answers:</h2>
    <ul>
        <% for (int i = 0; i < userAnswers.size(); i++) { %>
        <li>Question <%= (i + 1) %>: <%= userAnswers.get(i) %></li>
        <% } %>
    </ul>
    <% } %>

    <!-- Add any additional content or navigation -->
</div>
</body>
</html>
