<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
  <title>Blog Details - QuizLoco</title>
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
    <h2>Blog Details</h2>
    <div class="blog-details">
      <h3>Blog Title</h3>
      <p>Author: John Doe</p>
      <p>Date: 2024-01-01</p>
      <p>Category: Science</p>
      <p>Summary: A detailed summary of the blog post...</p>
      <h4>Content</h4>
      <p>Here is the detailed content of the blog post...</p>
      <h4>Comments</h4>
      <ul>
        <li>Comment 1: Sample comment text...</li>
        <li>Comment 2: Sample comment text...</li>
        <li>Comment 3: Sample comment text...</li>
      </ul>
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