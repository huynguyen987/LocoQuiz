<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users,entity.classs,dao.ClassDAO" %>
<%@ page import="java.util.List" %>
<%
  session = request.getSession();
  Users currentUser = (Users) session.getAttribute("user");
  if (currentUser == null || !"teacher".equals(currentUser.getRoleName())) {
    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
    return;
  }

  // Fetch the list of classes taught by the current teacher
  ClassDAO classDAO = new ClassDAO();
  List<classs> classes = null;
  try {
    classes = classDAO.getClassByTeacherId(currentUser.getId());
  } catch (Exception e) {
    e.printStackTrace();
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Teacher Dashboard - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/teacher.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<!-- Header -->
<header>
  <div class="container">
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="logo">QuizLoco</a>
    <nav>
      <ul>
        <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp">Dashboard</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/my-classes.jsp">My Classes</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/my-quizzes.jsp">My Quizzes</a></li>
      </ul>
    </nav>
    <div class="user-info">
      <span>Welcome, <%= currentUser.getUsername() %></span>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>
    </div>
  </div>
</header>

<!-- Sidebar -->
<aside class="sidebar">
  <ul>
    <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/my-classes.jsp"><i class="fas fa-chalkboard"></i> My Classes</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/createClass.jsp"><i class="fas fa-plus"></i> Create Class</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/my-quizzes.jsp"><i class="fas fa-question-circle"></i> My Quizzes</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/createQuiz.jsp"><i class="fas fa-plus-circle"></i> Create Quiz</a></li>
  </ul>
</aside>

<!-- Main Content -->
<main>
  <div class="dashboard-content">
    <h1>Teacher Dashboard</h1>

    <!-- Dashboard Statistics -->
    <div class="stats-container">
      <div class="stat-card">
        <div class="stat-icon"><i class="fas fa-chalkboard"></i></div>
        <div class="stat-info">
          <h3>Total Classes</h3>
          <p><%= classes != null ? classes.size() : 0 %></p>
        </div>
      </div>
      <!-- You can add more stat cards for students, quizzes, etc. -->
    </div>

    <!-- Action Links -->
    <div class="action-links">
      <a href="<%= request.getContextPath() %>/jsp/createClass.jsp" class="action-btn"><i class="fas fa-plus"></i> Create New Class</a>
    </div>

    <!-- Classes List -->
    <section class="classes-section">
      <h2>Your Classes</h2>
      <% if (classes != null && !classes.isEmpty()) { %>
      <div class="classes-grid">
        <% for (classs classEntity : classes) { %>
        <div class="class-card">
          <h3><%= classEntity.getName() %></h3>
          <div class="class-actions">
            <a href="<%= request.getContextPath() %>/ClassDetailsServlet?classId=<%= classEntity.getId() %>"><i class="fas fa-eye"></i> View</a>
            <a href="<%= request.getContextPath() %>/EditClassServlet?classId=<%= classEntity.getId() %>"><i class="fas fa-edit"></i> Edit</a>
            <a href="<%= request.getContextPath() %>/EnrollStudentServlet?classId=<%= classEntity.getId() %>"><i class="fas fa-user-plus"></i> Enroll Students</a>
            <a href="<%= request.getContextPath() %>/AssignQuizServlet?classId=<%= classEntity.getId() %>"><i class="fas fa-clipboard-list"></i> Assign Quiz</a>
          </div>
        </div>
        <% } %>
      </div>
      <% } else { %>
      <p>You have not created any classes yet.</p>
      <% } %>
    </section>
  </div>
</main>

<!-- Footer -->
<footer>
  <p>&copy; 2024 QuizLoco. All rights reserved.</p>
</footer>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/teacher.js"></script>
</body>
</html>
