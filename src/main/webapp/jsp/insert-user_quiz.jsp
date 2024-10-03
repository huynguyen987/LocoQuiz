<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Thêm User Quiz</title>
</head>
<body>
<h2>Thêm User Quiz</h2>
<form action="<%= request.getContextPath() %>/UserQuizServlet?action=insertUserQuiz" method="post">
  <label for="user_id">User ID:</label>
  <input type="number" id="user_id" name="user_id" required><br><br>

  <label for="quiz_id">Quiz ID:</label>
  <input type="number" id="quiz_id" name="quiz_id" required><br><br>

  <label for="tag_id">Tag ID:</label>
  <input type="number" id="tag_id" name="tag_id" required><br><br>

  <input type="submit" name="submit" value="Thêm">
</form>

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
