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
        <li><a href="#about">About</a></li>
        <li><a href="#services">Services</a></li>
        <li><a href="#contact">Contact</a></li>
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
        <a href="LogoutServlet" class="btn-logout">Logout</a>
      </div>
      <%
      } else {
      %>
      <!-- Display login and register buttons if user is not logged in -->
      <a href="dangnhap.jsp" class="btn-login">Login</a>

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
  <section class="hero">
    <div class="container">
      <h2>Welcome to QuizLoco</h2>
      <p>Your ultimate quiz platform</p>
      <%
        if (username != null) {
      %>
      <p>Welcome back, <%= username %>! Enjoy your personalized quiz experience.</p>
      <%
      } else {
      %>
      <a href="dangki.jsp" class="btn btn-register">Register</a>
      <%
        }
      %>
    </div>
  </section>

  <section class="features" id="about">
    <div class="container">
      <h2>Our Features</h2>
      <div class="features-grid">
        <div class="feature">
          <h3>Blog Posts</h3>
          <p>List of blogs for all subjects.</p>
        </div>
        <div class="feature">
          <h3>Subject Lists</h3>
          <p>All the subjects available.</p>
        </div>
        <div class="feature">
          <h3>View Quiz</h3>
          <p>Work in progress...</p>
        </div>
      </div>
    </div>
  </section>

  <section class="cta" id="services">
    <div class="container">
      <h2>Join Us Today</h2>
      <p>Become a part of QuizLoco and test your knowledge with thousands of quizzes!</p>
      <a href="dangki.jsp" class="btn btn-cta">Sign Up Now</a>
    </div>
  </section>
</main>

<footer id="contact">
  <div class="container">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="js/script.js"></script>
</body>
</html>
