<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ page import="entity.quiz" %>
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
      <h3><%= ((quiz) request.getAttribute("quiz")).getName() %></h3>
      <p>Description: <%= ((quiz) request.getAttribute("quiz")).getDescription() %></p>
      <p>Created At: <%= ((quiz) request.getAttribute("quiz")).getCreated_at() %></p>
      <p>Updated At: <%= ((quiz) request.getAttribute("quiz")).getUpdated_at() %></p>
      <p>User ID: <%= ((quiz) request.getAttribute("quiz")).getUser_id() %></p>
      <p>Type ID: <%= ((quiz) request.getAttribute("quiz")).getType_id() %></p>
      <p>Answer: <%= ((quiz) request.getAttribute("quiz")).getAnswer() %></p>
      <p>Status: <%= ((quiz) request.getAttribute("quiz")).isStatus() %></p>
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