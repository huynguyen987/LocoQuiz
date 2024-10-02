<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Thêm User Quiz</title>
</head>
<body>
<h1>Thêm User Quiz</h1>
<form action="UserQuizServlet?action=insertUserQuiz" method="post">
  <input type="hidden" name="submit" value="true" />
  <label for="user_id">User ID:</label><br/>
  <input type="number" id="user_id" name="user_id" required/><br/>
  <label for="quiz_id">Quiz ID:</label><br/>
  <input type="number" id="quiz_id" name="quiz_id" required/><br/>
  <label for="tag_id">Tag ID:</label><br/>
  <input type="number" id="tag_id" name="tag_id" required/><br/><br/>
  <input type="submit" value="Thêm User Quiz"/>
</form>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>

