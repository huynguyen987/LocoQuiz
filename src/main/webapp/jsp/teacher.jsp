<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, entity.classs, dao.ClassDAO, dao.UsersDAO, dao.ClassUserDAO, dao.ClassQuizDAO, dao.QuizDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="entity.quiz" %>
<%
  // Session và xác thực người dùng
  session = request.getSession(false);
  Users currentUser = null;
  if (session != null) {
    currentUser = (Users) session.getAttribute("user");
  }
  if (currentUser == null || !"teacher".equalsIgnoreCase(currentUser.getRoleName())) {
    response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
    return;
  }

  // Lấy danh sách lớp học của giáo viên hiện tại
  ClassDAO classDAO = new ClassDAO();
  List<classs> classes = null;
  try {
    classes = classDAO.getClassByTeacherId(currentUser.getId());
  } catch (Exception e) {
    e.printStackTrace();
  }

  // Lấy tham số 'action' để xác định phần nội dung hiển thị
  String action = request.getParameter("action");
  if (action == null) {
    action = "dashboard"; // Mặc định
  }

  // Lấy tham số 'message' để hiển thị thông báo
  String message = request.getParameter("message");
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
    <div class="user-info">
      <span>Welcome, <%= currentUser.getUsername() %></span>
      <a href="<%= request.getContextPath() %>/LogoutServlet" class="logout-btn">Logout</a>
      <a href="<%= request.getContextPath() %>/jsp/edit-profile.jsp" class="dashboard-link">Profile</a>
    </div>
  </div>
</header>

<!-- Sidebar -->
<aside class="sidebar">
  <ul>
    <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
    <li><a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=createClass"><i class="fas fa-plus"></i> Create Class</a></li>
    <li><a href="<%= request.getContextPath() %>/QuizController"><i class="fas fa-plus"></i> Create Quiz</a></li>
    <li><a href="<%= request.getContextPath() %>/AllQuizzesServlet"><i class="button"></i>View Quiz</a>
    </li>
  </ul>
</aside>

<!-- Main Content -->
<main>
  <div class="dashboard-content">
    <!-- Display success or error messages -->
    <% if (message != null) {
      if ("classCreated".equals(message)) { %>
    <div class="success-message">
      Class created successfully.
    </div>
    <% } else if ("classEdited".equals(message)) { %>
    <div class="success-message">
      Class edited successfully.
    </div>
    <% } else if ("createError".equals(message)) { %>
    <div class="error-message">
      An error occurred while creating the class. Please try again.
    </div>
    <% } else if ("editError".equals(message)) { %>
    <div class="error-message">
      An error occurred while editing the class. Please try again.
    </div>
    <% } else { %>
    <div class="info-message">
      <%= message %>
    </div>
    <% }
    } %>

    <% if ("dashboard".equals(action)) { %>
    <!-- Dashboard View -->
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
      <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=createClass" class="action-btn">
        <i class="fas fa-plus"></i> Create New Class
      </a>
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
            <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>">
              <i class="fas fa-eye"></i> View
            </a>
          </div>
        </div>
        <% } %>
      </div>
      <% } else { %>
      <p>You have not created any classes yet.</p>
      <% } %>
    </section>
    <% } else if ("classDetails".equals(action)) { %>
    <!-- Class Details View -->
    <%
      String classIdStr = request.getParameter("classId");
      if (classIdStr == null || classIdStr.trim().isEmpty()) {
        out.println("<p>Class ID is missing.</p>");
        return;
      }

      int classId;
      try {
        classId = Integer.parseInt(classIdStr);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        out.println("<p>Invalid Class ID.</p>");
        return;
      }

      classs classEntity = classDAO.getClassById(classId);
      if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
        UsersDAO usersDAO = new UsersDAO();
        ClassUserDAO classUserDAO = new ClassUserDAO();
        ClassQuizDAO classQuizDAO = new ClassQuizDAO();
        Users teacher = usersDAO.getUserById(classEntity.getTeacher_id());
        List<Users> students = classUserDAO.getUsersByClassId(classId);
        List<quiz> quizzes = classQuizDAO.getQuizzesByClassId(classId);
    %>
    <h1>Class Details: <%= classEntity.getName() %></h1>
    <div class="class-info">
      <p><strong>Description:</strong> <%= classEntity.getDescription() %></p>
      <p><strong>Class Code:</strong> <%= classEntity.getClass_key() %></p>
      <p><strong>Teacher:</strong> <%= teacher.getUsername() %></p>
    </div>

    <!-- Action Links -->
    <div class="action-links">
      <a href="<%= request.getContextPath() %>/EditClassServlet?classId=<%= classEntity.getId() %>" class="action-btn">
        <i class="fas fa-edit"></i> Edit Class
      </a>
      <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=assignQuiz&classId=<%= classEntity.getId() %>" class="action-btn">
        <i class="fas fa-clipboard-list"></i> Assign Quiz
      </a>
      <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=enrollStudents&classId=<%= classEntity.getId() %>" class="action-btn">
        <i class="fas fa-user-plus"></i> Enroll Students
      </a>
      <form action="<%=request.getContextPath()%>/DeleteClassServlet" method="post" onsubmit="return confirm('Are you sure you want to delete this class?');" style="display:inline;">
        <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
        <button type="submit" class="delete-button">
          <i class="fas fa-trash"></i> Delete Class
        </button>
      </form>
    </div>

    <!-- Students Section -->
    <h2>Enrolled Students</h2>
    <% if (students != null && !students.isEmpty()) { %>
    <ul>
      <% for (Users student : students) { %>
      <li><%= student.getUsername() %> (<%= student.getEmail() %>)</li>
      <% } %>
    </ul>
    <% } else { %>
    <p>No students enrolled in this class yet.</p>
    <% } %>

    <!-- Quizzes Section -->
    <h2>Assigned Quizzes</h2>
    <% if (quizzes != null && !quizzes.isEmpty()) { %>
    <ul>
      <% for (quiz quiz : quizzes) { %>
      <li><%= quiz.getName() %></li>
      <% } %>
    </ul>
    <% } else { %>
    <p>No quizzes assigned to this class yet.</p>
    <% } %>

    <!-- Back to Dashboard Link -->
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Dashboard
    </a>
    <% } else { %>
    <p>Class not found or access denied.</p>
    <% } %>
    <% } else if ("assignQuiz".equals(action)) { %>
    <!-- Assign Quiz View -->
    <%
      String classIdStr = request.getParameter("classId");
      if (classIdStr == null || classIdStr.trim().isEmpty()) {
        out.println("<p>Class ID is missing.</p>");
        return;
      }

      int classId;
      try {
        classId = Integer.parseInt(classIdStr);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        out.println("<p>Invalid Class ID.</p>");
        return;
      }

      classs classEntity = classDAO.getClassById(classId);
      if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
        // Get quizzes
        QuizDAO quizDAO = new QuizDAO();
        List<quiz> quizzes = quizDAO.getQuizzesByUserId(currentUser.getId());
    %>
    <h1>Assign Quiz to Class: <%= classEntity.getName() %></h1>
    <form action="<%=request.getContextPath()%>/AssignQuizServlet" method="post" class="form-container">
      <input type="hidden" name="classId" value="<%= classEntity.getId() %>">

      <label for="quizId">Select Quiz:</label>
      <select id="quizId" name="quizId" required>
        <% for (quiz quiz : quizzes) { %>
        <option value="<%= quiz.getId() %>"><%= quiz.getName() %></option>
        <% } %>
      </select>

      <button type="submit" class="submit-btn">
        <i class="fas fa-clipboard-list"></i> Assign Quiz
      </button>
    </form>
    <% if ("assignError".equals(message)) { %>
    <p class="error-message">An error occurred while assigning the quiz. Please try again.</p>
    <% } %>

    <!-- Back to Class Details Link -->
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Class Details
    </a>
    <% } else { %>
    <p>Class not found or access denied.</p>
    <% } %>
    <% } else if ("enrollStudents".equals(action)) { %>
    <!-- Enroll Students View -->
    <%
      String classIdStr = request.getParameter("classId");
      if (classIdStr == null || classIdStr.trim().isEmpty()) {
        out.println("<p>Class ID is missing.</p>");
        return;
      }

      int classId;
      try {
        classId = Integer.parseInt(classIdStr);
      } catch (NumberFormatException e) {
        e.printStackTrace();
        out.println("<p>Invalid Class ID.</p>");
        return;
      }

      classs classEntity = classDAO.getClassById(classId);
      if (classEntity != null && classEntity.getTeacher_id() == currentUser.getId()) {
        // Get students not enrolled
        UsersDAO usersDAO = new UsersDAO();
        List<Users> students = usersDAO.getAllStudentsNotInClass(classId);
    %>
    <h1>Enroll Students to Class: <%= classEntity.getName() %></h1>
    <form action="<%=request.getContextPath()%>/EnrollStudentServlet" method="post" class="form-container">
      <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
      <label for="studentId">Select Student:</label>
      <select id="studentId" name="studentId" required>
        <% for (Users student : students) { %>
        <option value="<%= student.getId() %>"><%= student.getUsername() %> (<%= student.getEmail() %>)</option>
        <% } %>
      </select>

      <button type="submit" class="submit-btn">
        <i class="fas fa-user-plus"></i> Enroll Student
      </button>
    </form>
    <% if ("enrollError".equals(message)) { %>
    <p class="error-message">An error occurred while enrolling the student. Please try again.</p>
    <% } %>

    <!-- Back to Class Details Link -->
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp?action=classDetails&classId=<%= classEntity.getId() %>" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Class Details
    </a>
    <% } else { %>
    <p>Class not found or access denied.</p>
    <% } %>
    <% } else if ("createClass".equals(action)) { %>
    <!-- Create Class View -->
    <h1>Create New Class</h1>
    <form action="<%=request.getContextPath()%>/CreateClassServlet" method="post" class="form-container">
      <label for="name">Class Name:</label>
      <input type="text" id="name" name="name" required>

      <label for="description">Description:</label>
      <textarea id="description" name="description"></textarea>

      <button type="submit" class="submit-btn">
        <i class="fas fa-plus"></i> Create Class
      </button>
    </form>
    <% if ("createError".equals(message)) { %>
    <p class="error-message">An error occurred while creating the class. Please try again.</p>
    <% } %>

    <!-- Back to Dashboard Link -->
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Dashboard
    </a>
    <% } else { %>
    <!-- Default or Unknown Action -->
    <p>Invalid action specified.</p>
    <!-- Optionally redirect to dashboard -->
    <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="action-btn back-btn">
      <i class="fas fa-arrow-left"></i> Back to Dashboard
    </a>
    <% } %>
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
