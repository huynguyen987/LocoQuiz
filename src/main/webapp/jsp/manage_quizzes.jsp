<%@ page import="java.util.List" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="entity.quiz" %>
<%@ page import="java.sql.SQLException" %>
<%
  // Initialize QuizDAO
  QuizDAO quizDAO = new QuizDAO();

  // Fetch list of quizzes
  List<quiz> quizList;
  try {
    quizList = quizDAO.getAllQuizzes(0, 50); // Fetch the first 50 quizzes
  } catch (SQLException | ClassNotFoundException e) {
    throw new RuntimeException(e);
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Manage Quizzes</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
<header>
  <h1>Manage Quizzes</h1>
</header>

<div class="quiz-management">
  <h2>Quiz List</h2>
  <table>
    <thead>
    <tr>
      <th>Quiz ID</th>
      <th>Title</th>
      <th>Description</th>
      <th>Creator ID</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% for (quiz q : quizList) { %>
    <tr>
      <td><%= q.getId() %></td>
      <td><%= q.getQuizTitle() %></td>
      <td><%= q.getDescription() %></td>
      <td><%= q.getCreatorId() %></td>
      <td><%= q.isStatus() ? "Visible" : "Hidden" %></td>
      <td>
        <form action="QuizServlet" method="post">
          <input type="hidden" name="quizId" value="<%= q.getId() %>">
          <button type="submit" name="action" value="updateQuiz">Edit</button>
          <button type="submit" name="action" value="toggleVisibility">
            <%= q.isStatus() ? "Hide" : "Show" %>
          </button>
          <button type="submit" name="action" value="deleteQuiz" onclick="return confirm('Are you sure you want to delete this quiz?');">Delete</button>
        </form>
      </td>
    </tr>
    <% } %>
    </tbody>
  </table>
</div>

<footer>
  <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
