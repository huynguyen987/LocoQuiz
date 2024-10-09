<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="entity.quiz, entity.Tag" %>
<%@ page import="dao.QuizDAO" %>
<%
  int quizId = Integer.parseInt(request.getParameter("id"));
  QuizDAO quizDAO = new QuizDAO();
  quiz q = quizDAO.getQuizById(quizId);
%>
<!DOCTYPE html>
<html>
<head>
  <title><%= q.getName() %> - Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
</head>
<body>
<!-- Include header -->
<header>
  <div class="container">
    <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/AllQuizzesServlet">All Quizzes</a></li>
        <!-- Add other navigation links as needed -->
      </ul>
    </nav>
  </div>
</header>

<main class="container">
  <h2><%= q.getName() %></h2>
  <div class="quiz-details">
    <p><strong>Description:</strong> <%= q.getDescription() %></p>
    <p><strong>Created At:</strong> <%= q.getCreated_at() %></p>
    <p><strong>Updated At:</strong> <%= q.getUpdated_at() %></p>
    <p><strong>Views:</strong> <%= q.getViews() %></p>
    <p><strong>Type:</strong> <%= q.getType_id() %></p>
    <p><strong>Status:</strong> <%= q.isStatus() ? "Active" : "Inactive" %></p>

    <!-- Display Tags -->
    <h3>Tags:</h3>
    <ul>
      <% for (Tag tag : q.getTag()) { %>
      <li><%= tag.getName() %></li>
      <% } %>
    </ul>

    <!-- Display other quiz details as needed -->
  </div>
  <!-- Optionally, add a link to take the quiz -->
  <a href="${pageContext.request.contextPath}/TakeQuizServlet?id=<%= q.getId() %>" class="btn-quiz">Take Quiz</a>
</main>

<!-- Include footer -->
<footer id="contact">
  <div class="container">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
