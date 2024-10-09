<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs, entity.quiz, java.util.List" %>
<%
  classs classEntity = (classs) request.getAttribute("classEntity");
  List<quiz> quizzes = (List<quiz>) request.getAttribute("quizzes");
%>
<!DOCTYPE html>
<html>
<head>
  <title>Assign Quiz to Class</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
  <!-- Include your styles here -->
</head>
<body>
<div class="container">
  <h1>Assign Quiz to Class: <%= classEntity.getName() %></h1>
  <form action="<%=request.getContextPath()%>/AssignQuizServlet" method="post">
    <input type="hidden" name="classId" value="<%= classEntity.getId() %>">

    <label for="quizId">Select Quiz:</label>
    <select id="quizId" name="quizId">
      <% for (quiz quiz : quizzes) { %>
      <option value="<%= quiz.getId() %>"><%= quiz.getName() %></option>
      <% } %>
    </select>

    <button type="submit">Assign Quiz</button>
  </form>
  <% if (request.getAttribute("errorMessage") != null) { %>
  <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
  <% } %>
</div>
</body>
</html>
