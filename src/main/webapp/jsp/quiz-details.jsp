<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="entity.quiz, entity.Tag" %>
<%@ page import="dao.QuizDAO" %>
<%
  int quizId = Integer.parseInt(request.getParameter("id"));
  QuizDAO quizDAO = new QuizDAO();
  quiz q = quizDAO.getQuizById(quizId);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title><%= q.getName() %> - Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-details.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
  <!-- Google Fonts (Optional for better typography) -->
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
  <!-- Font Awesome for Icons -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>

<main class="container">
  <div class="quiz-header">
    <h1><%= q.getName() %></h1>
    <div class="quiz-meta">
      <span><i class="fas fa-calendar-alt"></i> Created At: <%= q.getCreated_at() %></span>
      <span><i class="fas fa-sync-alt"></i> Updated At: <%= q.getUpdated_at() %></span>
      <span><i class="fas fa-eye"></i> Views: <%= q.getViews() %></span>
      <span><i class="fas fa-tags"></i> Type: <%= q.getType_id() %></span>
      <span><i class="fas fa-circle <% if(q.isStatus()) { %>active<% } else { %>inactive<% } %>"></i> <%= q.isStatus() ? "Active" : "Inactive" %></span>
    </div>
  </div>

  <section class="quiz-details">
    <h2>Description</h2>
    <p><%= q.getDescription() %></p>

    <!-- Display Tags -->
    <div class="quiz-tags">
      <h3>Tags:</h3>
      <ul>
        <% for (Tag tag : q.getTag()) { %>
        <li><a href="${pageContext.request.contextPath}/SearchByTagServlet?tag=<%= tag.getName() %>"><%= tag.getName() %></a></li>
        <% } %>
      </ul>
    </div>

    <!-- Action Buttons -->
    <div class="quiz-actions">
      <a href="${pageContext.request.contextPath}/TakeQuizServlet?id=<%= q.getId() %>" class="btn-quiz">Take Quiz</a>
      <%
        String role = (String) session.getAttribute("role");
        if ("admin".equalsIgnoreCase(role)) {
      %>
      <a href="${pageContext.request.contextPath}/EditQuizServlet?id=<%= q.getId() %>" class="btn-edit">Edit Quiz</a>
      <a href="${pageContext.request.contextPath}/DeleteQuizServlet?id=<%= q.getId() %>" class="btn-delete" onclick="return confirm('Are you sure you want to delete this quiz?');">Delete Quiz</a>
      <% } %>
    </div>
  </section>
</main>


<!-- Include JavaScript -->
<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
