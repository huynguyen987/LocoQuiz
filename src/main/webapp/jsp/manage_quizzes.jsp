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
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<%
  // Initialize QuizDAO
  QuizDAO quizDAO = new QuizDAO();

  // Fetch list of quizzes
  List<quiz> quizList = null;
  try {
    quizList = quizDAO.getAllQuizzes(0, 50); // Fetch the first 50 quizzes
  } catch (SQLException | ClassNotFoundException e) {
    e.printStackTrace();
  }
%>

<div class="container">
  <!-- Sidebar Navigation -->
  <aside class="sidebar">
    <h2>Admin Panel</h2>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/jsp/admin.jsp">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/manage_users.jsp">Manage Users</a></li>
        <li class="active"><a href="${pageContext.request.contextPath}/jsp/manage_quizzes.jsp">Manage Quizzes</a></li>
        <li><a href="${pageContext.request.contextPath}/LogoutServlet">Logout</a></li>
      </ul>
    </nav>
  </aside>

  <!-- Main Content -->
  <main class="main-content">
    <!-- Header Section -->
    <header>
      <h1>Quiz Management</h1>
    </header>

    <!-- Display Messages -->
    <c:if test="${not empty param.message}">
      <div class="alert success">${param.message}</div>
    </c:if>
    <c:if test="${not empty param.error}">
      <div class="alert error">${param.error}</div>
    </c:if>

    <!-- Quiz Table -->
    <div class="table-responsive">
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
        <% if (quizList != null) {
          if (!quizList.isEmpty()) {
            for (quiz q : quizList) { %>
        <tr>
          <td><%= q.getId() %></td>
          <td><%= q.getName() %></td>
          <td><%= q.getDescription() %></td>
          <td><%= q.getUser_id() %></td>
          <td><%= q.isStatus() ? "Visible" : "Hidden" %></td>
          <td class="actions">
            <form action="${pageContext.request.contextPath}/QuizServlet" method="post">
              <input type="hidden" name="quizId" value="<%= q.getId() %>">
              <button type="submit" name="action" value="toggleVisibility" class="action-btn toggle">
                <%= q.isStatus() ? "Hide" : "Show" %>
              </button>
            </form>
          </td>
        </tr>
        <%      }
        } else { %>
        <tr>
          <td colspan="6">No quizzes found.</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>
  </main>
</div>

<!-- Footer Section -->
<footer>
  <p>© 2023 Quiz Admin Dashboard. All rights reserved.</p>
</footer>

<script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
<% } %>