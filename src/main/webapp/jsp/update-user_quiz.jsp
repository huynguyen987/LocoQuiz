<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.user_quiz" %>
<html>
<head>
  <title>Cập Nhật User Quiz</title>
</head>
<body>
<h2>Cập Nhật User Quiz</h2>
<%
  // Lấy đối tượng user_quiz cũ từ request
  user_quiz oldUserQuiz = (user_quiz) request.getAttribute("oldUserQuiz");
  if (oldUserQuiz != null) {
%>
<form action="<%= request.getContextPath() %>/UserQuizServlet?action=updateUserQuiz" method="post">
  <!-- Thông tin cũ, không cho phép chỉnh sửa -->
  <input type="hidden" name="old_user_id" value="<%= oldUserQuiz.getUser_id() %>">
  <input type="hidden" name="old_quiz_id" value="<%= oldUserQuiz.getQuiz_id() %>">
  <input type="hidden" name="old_tag_id" value="<%= oldUserQuiz.getTag_id() %>">

  <label for="user_id">User ID mới:</label>
  <input type="number" id="user_id" name="user_id" value="<%= oldUserQuiz.getUser_id() %>" required><br><br>

  <label for="quiz_id">Quiz ID mới:</label>
  <input type="number" id="quiz_id" name="quiz_id" value="<%= oldUserQuiz.getQuiz_id() %>" required><br><br>

  <label for="tag_id">Tag ID mới:</label>
  <input type="number" id="tag_id" name="tag_id" value="<%= oldUserQuiz.getTag_id() %>" required><br><br>

  <input type="submit" name="submit" value="Cập Nhật">
</form>
<%
} else {
%>
<p style="color:red;">Không tìm thấy bản ghi để cập nhật.</p>
<%
  }
%>

<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
<p style="color:red;"><%= error %></p>
<%
  }
%>

<a href="<%= request.getContextPath() %>/UserQuizServlet">Quay lại</a>
</body>
</html>
