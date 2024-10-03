<!-- File: web/quiz-creator.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.Tag" %>
<!DOCTYPE html>
<html>
<head>
  <title>Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-creator.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
</head>

<body>
<header>
  <div class="container">
    <h1><a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a></h1>
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#about">About</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#services">Services</a></li>
        <li><a href="${pageContext.request.contextPath}/index.jsp#contact">Contact</a></li>
      </ul>
    </nav>
  </div>
</header>
<%
  // Retrieve the list of tags from the request attribute
  List<Tag> tagList = (List<Tag>) request.getAttribute("tagList");
  if (tagList == null) {
    tagList = new ArrayList<>();
  }
%>
<div class="container">
  <h1>Create a New Quiz</h1>
  <!-- Display error message if any -->
  <% String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null) { %>
  <div class="error-message"><%= errorMessage %></div>
  <% } %>
  <form action="QuizController" method="post">
    <!-- Quiz Name -->
    <label for="quizName">Quiz Name:</label>
    <input type="text" id="quizName" name="quizName" required>

    <!-- Description -->
    <label for="quizDescription">Description:</label>
    <textarea id="quizDescription" name="quizDescription" required></textarea>

    <!-- Tag -->
    <label for="quizTag">Tag:</label>
    <select id="quizTag" name="quizTag" required>
      <% for (Tag tag : tagList) { %>
      <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
      <% } %>
    </select>

    <!-- Question Content -->
    <label for="questionContent">Question:</label>
    <textarea id="questionContent" name="questionContent" required></textarea>

    <!-- Answers -->
    <label>Answers:</label>
    <div class="answer-option">
      <input type="radio" name="correctAnswer" value="1" required>
      <input type="text" name="answer1" placeholder="Answer 1" required>
    </div>
    <div class="answer-option">
      <input type="radio" name="correctAnswer" value="2">
      <input type="text" name="answer2" placeholder="Answer 2" required>
    </div>
    <div class="answer-option">
      <input type="radio" name="correctAnswer" value="3">
      <input type="text" name="answer3" placeholder="Answer 3" required>
    </div>
    <div class="answer-option">
      <input type="radio" name="correctAnswer" value="4">
      <input type="text" name="answer4" placeholder="Answer 4" required>
    </div>

    <!-- Submit Button -->
    <input type="submit" value="Create Quiz">
  </form>
</div>
</body>
</html>
