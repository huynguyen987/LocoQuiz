<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>QuizLoco</title>
  <link rel="stylesheet" href="css/styles.css">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
<header>
  <div class="container">
    <h1><a href="index.jsp" class="logo">QuizLoco</a></h1>
    <nav>
      <ul>
        <li><a href="index.jsp">Home</a></li>
        <li><a href="index.jsp#about">About</a></li>
        <li><a href="index.jsp#services">Services</a></li>
        <li><a href="index.jsp#contact">Contact</a></li>
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
        <p>Hello, <a href="${pageContext.request.contextPath}/jsp/user-profile.jsp"><%= username %></a>!</p>
        <a href="LogoutServlet" class="btn-logout">Logout</a>
      </div>
      <%
      } else {
      %>
      <!-- Display login button if user is not logged in -->
      <a href="jsp/login.jsp" class="btn-login">Login</a>
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
    <aside class="sidebar">
      <h2>Quick Links</h2>
      <ul>
        <li><a href="${pageContext.request.contextPath}/jsp/blog-lists.jsp">Blog Lists</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/quiz-lists.jsp">Quiz Lists</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/view-lessons.jsp">View Quizzes</a></li>
      </ul>
    </aside>
    <div class="main-content">
      <!-- Hero Section -->
      <section class="hero">
        <div class="hero-content">
          <h2>Welcome to QuizLoco</h2>
          <p>Your ultimate quiz platform</p>
          <%
            if (username != null) {
          %>
          <p>Welcome back, <%= username %>! Enjoy your personalized quiz experience.</p>
          <%
          } else {
          %>
          <a href="jsp/register.jsp" class="btn btn-register">Register</a>
          <%
            }
          %>
        </div>
      </section>

      <!-- Features Section -->
      <section class="features" id="about">
        <div class="container">
          <h2>Our Features</h2>
          <div class="features-grid">
            <div class="feature">
              <h3>Blog Posts</h3>
              <p>Explore insightful blogs on various subjects.</p>
            </div>
            <div class="feature">
              <h3>Quiz Lists</h3>
              <p>Browse all available quizzes.</p>
            </div>
            <div class="feature">
              <h3>View Quizzes</h3>
              <p>Test your knowledge with our quizzes.</p>
            </div>
          </div>
        </div>
      </section>

      <!-- Call to Action Section -->
      <section class="cta" id="services">
        <div class="container">
          <h2>Join Us Today</h2>
          <p>Become a part of QuizLoco and challenge yourself!</p>
          <a href="jsp/register.jsp" class="btn btn-cta">Sign Up Now</a>
        </div>
      </section>
    </div> <!-- End of main-content -->
  </div> <!-- End of content-with-sidebar -->
</main>

<footer id="contact">
  <div class="container">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="js/script.js"></script>
</body>
</html>
