<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, entity.classs, entity.quiz, java.util.List" %>
<%
  classs classEntity = (classs) request.getAttribute("classEntity");
  Users teacher = (Users) request.getAttribute("teacher");
  List<Users> students = (List<Users>) request.getAttribute("students");
  List<quiz> quizzes = (List<quiz>) request.getAttribute("quizzes");
  Users currentUser = (Users) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Chi Tiết Lớp</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
  <!-- Include your styles here -->
</head>
<body>
<div class="container">
  <h1>Chi Tiết Lớp: <%= classEntity.getName() %></h1>
  <p><strong>Mô Tả:</strong> <%= classEntity.getDescription() %></p>
  <p><strong>Mã Lớp:</strong> <%= classEntity.getClass_key() %></p>
  <p><strong>Giáo Viên:</strong> <%= teacher.getUsername() %></p>

  <div class="section">
    <h2>Danh Sách Học Sinh</h2>
    <ul>
      <% for (Users student : students) { %>
      <li><%= student.getUsername() %> (<%= student.getEmail() %>)</li>
      <% } %>
    </ul>
  </div>

  <div class="section">
    <h2>Bài Kiểm Tra</h2>
    <ul>
      <% for (quiz quiz : quizzes) { %>
      <li><%= quiz.getName() %></li>
      <% } %>
    </ul>
  </div>

  <% if (currentUser.getRole_id() == 1 || currentUser.getRole_id() == 3) { %>
  <div class="action-links">
    <a href="<%=request.getContextPath()%>/jsp/editClass.jsp?classId=<%= classEntity.getId() %>">Chỉnh Sửa Lớp</a>
    <a href="<%=request.getContextPath()%>/jsp/assignQuiz.jsp?classId=<%= classEntity.getId() %>">Gán Bài Kiểm Tra</a>
    <a href="<%=request.getContextPath()%>/jsp/enrollStudent.jsp?classId=<%= classEntity.getId() %>">Ghi Danh Học Sinh</a>
    <form action="<%=request.getContextPath()%>/DeleteClassServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa lớp này?');" style="display:inline;">
      <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
      <button type="submit" class="delete-button">Xóa Lớp</button>
    </form>
  </div>
  <% } %>
</div>
</body>
</html>
