<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Users" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="dao.ClassUserDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chi tiết lớp học - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
  <div class="dashboard-content">
    <%
      // Lấy đối tượng classEntity từ request
      classs classEntity = (classs) request.getAttribute("classEntity");
      if (classEntity == null) {
        response.sendRedirect(request.getContextPath() + "/my-classes.jsp?error=" + java.net.URLEncoder.encode("Không tìm thấy lớp học.", "UTF-8"));
        return;
      }

      // Lấy danh sách các quiz được giao cho lớp học
      List<quiz> assignedQuizzes = null;
      try {
        QuizDAO quizDAO = new QuizDAO();
        assignedQuizzes = quizDAO.getAssignedQuizzesByClassId(classEntity.getId());
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        assignedQuizzes = null;
      }

      // Lấy danh sách sinh viên đã được duyệt trong lớp học
      List<Users> enrolledStudents = null;
      try {
        ClassUserDAO classUserDAO = new ClassUserDAO();
        enrolledStudents = classUserDAO.getStudentsByClassId(classEntity.getId());
      } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        enrolledStudents = null;
      }

      // Thông báo thành công hoặc lỗi
      String message = request.getParameter("message");
      String error = request.getParameter("error");
    %>

    <!-- Hiển thị thông báo -->
    <% if (message != null) { %>
    <div class="success-message"><%= message %></div>
    <% } %>
    <% if (error != null) { %>
    <div class="error-message"><%= error %></div>
    <% } %>

    <div class="class-info-container">
      <!-- Hộp thông tin lớp học -->
      <div class="class-info-box">
        <h1>Chi tiết lớp học: <%= classEntity.getName() %></h1>
        <p><strong>Mô tả:</strong> <%= classEntity.getDescription() %></p>
        <p><strong>Mã lớp học:</strong> <%= classEntity.getClass_key() %></p>
        <p><strong>Giáo viên:</strong> <%= classEntity.getTeacher_name() %></p>
      </div>

      <!-- Hộp chứa các nút hành động -->
      <div class="class-action-buttons">
        <a href="<%= request.getContextPath() %>/CreateCompetitionServlet?classId=<%= classEntity.getId() %>" class="button create-competition-btn">
          <i class="fas fa-trophy"></i> Tạo Cuộc Thi
        </a>
        <a href="<%= request.getContextPath() %>/CreateQuizServlet?classId=<%= classEntity.getId() %>" class="button create-quiz-btn">
          <i class="fas fa-plus"></i> Tạo Quiz
        </a>
      </div>

      <!-- Ô thông báo -->
      <div class="notification-area">
        <% if (message != null) { %>
        <div class="success-message"><%= message %></div>
        <% } %>
        <% if (error != null) { %>
        <div class="error-message"><%= error %></div>
        <% } %>
      </div>
    </div>


    <!-- Danh sách các quiz được giao -->
      <div class="class-details-sections">
      <section id="assigned-quizzes">
        <h2>Danh sách Quiz</h2>
        <% if (assignedQuizzes != null && !assignedQuizzes.isEmpty()) { %>
        <ul class="quiz-list">
          <% for (quiz q : assignedQuizzes) { %>
          <li>
            <h3><%= q.getName() %></h3>
            <p><%= q.getDescription() %></p>
            <!-- Nút làm quiz -->
            <form action="<%= request.getContextPath() %>/TakeQuizServlet" method="get">
              <input type="hidden" name="id" value="<%= q.getId() %>">
              <button type="submit" class="button">Làm Quiz</button>
            </form>
          </li>
          <% } %>
        </ul>
        <% } else { %>
        <p>Không có quiz nào được giao cho lớp học này.</p>
        <% } %>
      </section>

    <!-- Danh sách sinh viên đã được duyệt -->
    <section id="enrolled-students">
      <h2>Sinh viên đã ghi danh</h2>
      <% if (enrolledStudents != null && !enrolledStudents.isEmpty()) { %>
      <table class="students-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Tên Sinh viên</th>
          <th>Email</th>
          <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <% for (Users student : enrolledStudents) { %>
        <tr>
          <td><%= student.getId() %></td>
          <td><%= student.getUsername() %></td>
          <td><%= student.getEmail() %></td>
          <td>
            <!-- Nút xóa sinh viên khỏi lớp học -->
            <form action="<%= request.getContextPath() %>/DeleteStudentServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sinh viên này khỏi lớp học?');">
              <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
              <input type="hidden" name="userId" value="<%= student.getId() %>">
              <button type="submit" class="button delete-button">
                <i class="fas fa-trash"></i> Xóa
              </button>
            </form>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>
      <% } else { %>
      <p>Không có sinh viên nào được ghi danh trong lớp học này.</p>
      <% } %>
    </section>
      </div>

    <!-- Nút quay lại danh sách lớp học -->
    <div class="action-buttons">
      <a href="<%= request.getContextPath() %>/my-classes.jsp" class="button back-btn">
        <i class="fas fa-arrow-left"></i> Quay lại Lớp học
      </a>
    </div>
  </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
</body>
</html>