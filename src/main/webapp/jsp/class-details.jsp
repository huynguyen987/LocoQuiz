<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs" %>
<%@ page import="entity.quiz" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chi tiết lớp học - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
  <div class="dashboard-content">
    <%
      classs classEntity = (classs) request.getAttribute("classEntity");
      if (classEntity == null) {
        response.sendRedirect(request.getContextPath() + "/my-classes.jsp?error=" + java.net.URLEncoder.encode("Không tìm thấy lớp học.", "UTF-8"));
        return;
      }

      // Lấy danh sách các quiz được giao cho lớp học
      List<quiz> assignedQuizzes = null;
      try {
        QuizDAO quizDAO = new QuizDAO();
        assignedQuizzes = quizDAO.getAssignedQuizzesByClassId(classEntity.getId());
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        assignedQuizzes = null;
      }
    %>
    <h1>Chi tiết lớp học: <%= classEntity.getName() %></h1>
    <p><strong>Mô tả:</strong> <%= classEntity.getDescription() %></p>

    <!-- Danh sách các quiz được giao -->
    <h2>Danh sách Quiz</h2>
    <% if (assignedQuizzes != null && !assignedQuizzes.isEmpty()) { %>
    <ul class="quiz-list">
      <% for (quiz q : assignedQuizzes) { %>
      <li>
        <h3><%= q.getName() %></h3>
        <p><%= q.getDescription() %></p>
        <!-- Nút làm quiz -->
        <form action="<%= request.getContextPath() %>/TakeQuizServlet" method="get">
          <input type="hidden" name="id" value="<%= q.getId() %>">
          <button type="submit" class="button">Làm Quiz</button>
        </form>
      </li>
      <% } %>
    </ul>
    <% } else { %>
    <p>Không có quiz nào được giao cho lớp học này.</p>
    <% } %>
  </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
</body>
</html>
