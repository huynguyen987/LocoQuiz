<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="entity.user_quiz"%>
<%
  user_quiz oldUserQuiz = (user_quiz) request.getAttribute("oldUserQuiz");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Cập Nhật User Quiz</title>
</head>
<body>
<h1>Cập Nhật User Quiz</h1>
<form action="UserQuizServlet?action=updateUserQuiz" method="post">
  <input type="hidden" name="submit" value="true" />
  <!-- Truyền các giá trị cũ để xác định bản ghi cần cập nhật -->
  <input type="hidden" name="old_user_id" value="<%= oldUserQuiz.getUser_id() %>" />
  <input type="hidden" name="old_quiz_id" value="<%= oldUserQuiz.getQuiz_id() %>" />
  <input type="hidden" name="old_tag_id" value="<%= oldUserQuiz.getTag_id() %>" />

  <label for="user_id">User ID:</label><br/>
  <input type="number" id="user_id" name="user_id" value="<%= oldUserQuiz.getUser_id() %>" required/><br/>
  <label for="quiz_id">Quiz ID:</label><br/>
  <input type="number" id="quiz_id" name="quiz_id" value="<%= oldUserQuiz.getQuiz_id() %>" required/><br/>
  <label for="tag_id">Tag ID:</label><br/>
  <input type="number" id="tag_id" name="tag_id" value="<%= oldUserQuiz.getTag_id() %>" required/><br/><br/>
  <input type="submit" value="Cập Nhật User Quiz"/>
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
