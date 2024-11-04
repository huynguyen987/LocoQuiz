<%@ page import="java.util.List" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="entity.quiz" %>
<%@ page import="java.sql.SQLException" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
  <nav>
    <ul>
      <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
      <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
      <li><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
    </ul>
  </nav>
</header>

<!-- Hiển thị thông báo nếu có -->
<c:if test="${not empty param.message}">
  <div class="success-message">${param.message}</div>
</c:if>
<c:if test="${not empty param.error}">
  <div class="error-message">${param.error}</div>
</c:if>

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
    <%
      // Initialize QuizDAO
      QuizDAO quizDAO = new QuizDAO();

      // Fetch list of quizzes
      List<quiz> quizList = null;
      try {
        quizList = quizDAO.getAllQuizzes(0, 50); // Fetch the first 50 quizzes
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        // Gửi thông báo lỗi nếu có
    %>
    <tr>
      <td colspan="6">Error fetching quizzes.</td>
    </tr>
    <%
      }
      if (quizList != null && !quizList.isEmpty()) {
        for (quiz q : quizList) {
    %>
    <tr>
      <td><%= q.getId() %></td>
      <td><%= q.getName() %></td>
      <td><%= q.getDescription() %></td>
      <td><%= q.getUser_id() %></td>
      <td><%= q.isStatus() ? "Visible" : "Hidden" %></td>
      <td>
        <form action="${pageContext.request.contextPath}/QuizServlet" method="post" style="display:inline;">
          <input type="hidden" name="quizId" value="<%= q.getId() %>">
          <!-- Sửa action thành 'toggleVisibility' -->
          <button type="submit" name="action" value="toggleVisibility">
            <%= q.isStatus() ? "Hide" : "Show" %>
          </button>

        </form>
      </td>
    </tr>
    <%
      }
    } else if (quizList != null) {
    %>
    <tr>
      <td colspan="6">No quizzes found.</td>
    </tr>
    <%
      }
    %>
    </tbody>
  </table>
</div>

<footer>
  <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
