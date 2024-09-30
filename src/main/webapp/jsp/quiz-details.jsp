<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
  <title>Quiz Details - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="${pageContext.request.contextPath}/css/fonts.css" rel="stylesheet">
</head>
<body>
<%-- Include header --%>
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

<main>
  <div class="container">
    <h2>Quiz Details</h2>
    <div class="quiz-details">
      <h3>Quiz Title</h3>
      <p>Description: A detailed description of the quiz.</p>
      <p>Category: Science</p>
      <p>Number of Questions: 10</p>
      <p>Difficulty: Medium</p>
      <p>Author: John Doe</p>
      <p>Date Created: 2024-01-01</p>
      <p>Average Rating: 4.5</p>
      <p>Number of Attempts: 150</p>
      <p>Tags: Science, General Knowledge</p>
      <p>Estimated Time: 15 minutes</p>
      <h4>Instructions</h4>
      <p>Here are the detailed instructions for the quiz...</p>
      <h4>Sample Questions</h4>
      <ul>
        <li>Question 1: Sample question text...</li>
        <li>Question 2: Sample question text...</li>
        <li>Question 3: Sample question text...</li>
      </ul>
      <div class="quiz-actions">
        <a href="start-quiz.jsp" class="btn btn-start">Start Quiz</a>
      </div>
    </div>
  </div>
</main>

<%-- Include footer --%>
<footer id="contact">
  <div class="container">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>