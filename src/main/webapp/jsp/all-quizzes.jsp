<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, entity.quiz, entity.Tag" %>
<%@ page import="dao.QuizDAO" %>
<%
  // Retrieve the username and role from the session
  String username = (String) session.getAttribute("username");
  String role = (String) session.getAttribute("role");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>All Quizzes - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <!-- Font Awesome -->
  <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
  <!-- Pass contextPath to JavaScript -->
  <script type="text/javascript">
    var contextPath = '<%= request.getContextPath() %>';
  </script>
</head>
<body id="body">
<!-- Header -->
<header>
  <div class="container">
    <!-- Sidebar Toggle Button -->
    <button id="sidebarToggle" class="sidebar-toggle-btn" aria-label="Open sidebar">
      <i class="fas fa-bars"></i>
    </button>
    <!-- Logo -->
    <a href="${pageContext.request.contextPath}/index.jsp" class="logo">QuizLoco</a>
    <!-- Navigation Menu -->
    <nav>
      <ul>
        <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/all-quizzes.jsp">All Quizzes</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/tag-list.jsp">Tag List</a></li>
        <%
          if (role != null && role.equals("student")) {
        %>
        <li><a href="${pageContext.request.contextPath}/jsp/my-classes.jsp">My Classes</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/recent-quizzes.jsp">Recent Quizzes</a></li>
        <%
        } else if (role != null && role.equals("teacher")) {
        %>
        <li><a href="${pageContext.request.contextPath}/jsp/my-classes.jsp">My Classes</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/create-class.jsp">Create Class</a></li>
        <%
          }
        %>
      </ul>
    </nav>
  </div>
    <!-- Search Container -->



    <!-- Authentication Links -->
    <div class="auth-links">
      <% if (username != null) { %>
      <div class="user-info">
        <a href="${pageContext.request.contextPath}/jsp/user-profile.jsp" class="user-profile">
          <i class="fas fa-user-circle"></i>
          <span class="username">Welcome, <%= username %></span>
        </a>
        <a href="${pageContext.request.contextPath}/jsp/logout.jsp" class="btn-logout">Logout</a>
      </div>
      <% } else { %>
      <a href="${pageContext.request.contextPath}/jsp/login.jsp" class="btn-login">Login</a>
      <a href="${pageContext.request.contextPath}/jsp/register.jsp" class="btn-register">Register</a>
      <% } %>
    </div>
    <!-- Mobile Menu Toggle and Theme Toggle -->




</header>

<!-- Main Content -->
<main>
  <div class="container">
    <h1>All Quizzes</h1>

    <%
      // Create an instance of QuizDAO to interact with the database
      QuizDAO quizDAO = new QuizDAO();

      // Retrieve the latest quizzes from the database
      List<quiz> latestQuizzes = quizDAO.getLatestQuizzes();

      // Retrieve the most popular quizzes from the database
      List<quiz> popularQuizzes = quizDAO.getPopularQuizzes();

      // Retrieve all quizzes, including those created via quiz-creator.jsp
      List<quiz> allQuizzes = quizDAO.getAllQuizzes();
    %>

    <!-- Latest Quizzes Section -->
    <section id="latest-quizzes" class="latest-quizzes">
      <h2>Latest Quizzes</h2>
      <div class="quiz-grid">
        <% if (latestQuizzes != null && !latestQuizzes.isEmpty()) {
          for (quiz quiz : latestQuizzes) { %>
        <div class="quiz-card">
          <img src="${pageContext.request.contextPath}/img/quiz.jpg" alt="<%= quiz.getName() %>" loading="lazy">
          <div class="quiz-content">
            <h3><%= quiz.getName() %></h3>
            <p><%= quiz.getDescription() %></p>
            <a href="${pageContext.request.contextPath}/jsp/quiz-detail.jsp?id=<%= quiz.getId() %>" class="btn-quiz">Take Quiz</a>
          </div>
        </div>
        <%   }
        } else { %>
        <p>No latest quizzes available.</p>
        <% } %>
      </div>
    </section>

    <!-- Popular Quizzes Section -->
    <section id="popular-quizzes" class="popular-quizzes">
      <h2>Popular Quizzes</h2>
      <div class="quiz-grid">
        <% if (popularQuizzes != null && !popularQuizzes.isEmpty()) {
          for (quiz quiz : popularQuizzes) { %>
        <div class="quiz-card">
          <img src="${pageContext.request.contextPath}/img/quiz.jpg" alt="<%= quiz.getName() %>" loading="lazy">
          <div class="quiz-content">
            <h3><%= quiz.getName() %></h3>
            <p><%= quiz.getDescription() %></p>
            <a href="${pageContext.request.contextPath}/jsp/quiz-detail.jsp?id=<%= quiz.getId() %>" class="btn-quiz">Take Quiz</a>
          </div>
        </div>
        <%   }
        } else { %>
        <p>No popular quizzes available.</p>
        <% } %>
      </div>
    </section>

    <!-- All Quizzes Section -->
    <section id="all-quizzes-section" class="all-quizzes">
      <h2>All Quizzes</h2>
      <div class="quiz-grid">
        <% if (allQuizzes != null && !allQuizzes.isEmpty()) {
          for (quiz quiz : allQuizzes) { %>
        <div class="quiz-card">
          <img src="${pageContext.request.contextPath}/img/quiz.jpg" alt="<%= quiz.getName() %>" loading="lazy">
          <div class="quiz-content">
            <h3><%= quiz.getName() %></h3>
            <p><%= quiz.getDescription() %></p>
            <a href="${pageContext.request.contextPath}/jsp/quiz-detail.jsp?id=<%= quiz.getId() %>" class="btn-quiz">Take Quiz</a>
          </div>
        </div>
        <%   }
        } else { %>
        <p>No quizzes available.</p>
        <% } %>
      </div>
    </section>
  </div>
</main>

<!-- Footer Section -->
<footer>
  <div class="container">
    <!-- Include your footer content here -->
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
  </div>
</footer>

<script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
