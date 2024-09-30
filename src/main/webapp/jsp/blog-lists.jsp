<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html>
<head>
  <title>Blog Lists - QuizLoco</title>
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

    <!-- Display login/register or user details based on session -->
    <div class="auth-links">
      <%
        String username = (String) session.getAttribute("username");
        if (username != null) {
      %>
      <!-- Display username and logout button when user is logged in -->
      <div class="user-info">
        <p>Hello, <%= username %>!</p>
        <a href="${pageContext.request.contextPath}/LogoutServlet" class="btn-logout">Logout</a>
      </div>
      <%
      } else {
      %>
      <!-- Display login button if user is not logged in -->
      <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="btn-login">Login</a>
      <%
        }
      %>
    </div>

    <div class="mobile-menu-toggle">
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</header>

<main>
  <div class="content-with-sidebar">
    <%-- Include sidebar --%>
    <aside class="sidebar">
      <h2>Quick Links</h2>
      <!-- Sidebar Links -->
      <ul>
        <li><a href="${pageContext.request.contextPath}/jsp/blog-lists.jsp">Blog Lists</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/quiz-lists.jsp">Quiz Lists</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/view-lessons.jsp">View Quizzes</a></li>
      </ul>
    </aside>
    <div class="main-content">
      <div class="container">
        <h2>Blog Posts</h2>
        <div class="blog-list">
          <div class="blog-item">
            <h3>Blog Title 1</h3>
            <p>Author: John Doe</p>
            <p>Date: 2024-01-01</p>
            <p>Category: Science</p>
            <p>Summary: A brief summary of the blog post...</p>
            <a href="blog-details.jsp" class="btn btn-read-more">Read More</a>
          </div>
          <div class="blog-item">
            <h3>Blog Title 2</h3>
            <p>Author: Jane Smith</p>
            <p>Date: 2024-02-15</p>
            <p>Category: History</p>
            <p>Summary: Another brief summary of the blog post...</p>
            <a href="blog-details.jsp" class="btn btn-read-more">Read More</a>
          </div>
          <div class="blog-item">
            <h3>Blog Title 3</h3>
            <p>Author: Alice Johnson</p>
            <p>Date: 2024-03-10</p>
            <p>Category: Technology</p>
            <p>Summary: Yet another brief summary of the blog post...</p>
            <a href="blog-details.jsp" class="btn btn-read-more">Read More</a>
          </div>
          <!-- Repeat for other blog posts -->
        </div>
      </div>
    </div> <!-- End of main-content -->
  </div> <!-- End of content-with-sidebar -->
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