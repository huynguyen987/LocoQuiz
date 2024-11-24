<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs" %>
<%@ page import="entity.quiz" %>
<%@ page import="entity.Users" %>
<%@ page import="entity.JoinRequest" %>
<%@ page import="dao.QuizDAO" %>
<%@ page import="dao.ClassUserDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Chi tiết lớp học - QuizLoco</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
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

      // Lấy danh sách yêu cầu tham gia (chỉ khi người dùng là giáo viên)
      List<JoinRequest> pendingRequests = null;
      Users currentUser = (Users) session.getAttribute("user");
      boolean isTeacher = currentUser != null && (currentUser.getRole_id() == Users.ROLE_TEACHER || currentUser.getRole_id() == Users.ROLE_ADMIN);

      if (isTeacher) {
        try {
          ClassUserDAO classUserDAO = new ClassUserDAO();
          pendingRequests = classUserDAO.getPendingJoinRequests(classEntity.getId());
        } catch (SQLException | ClassNotFoundException e) {
          e.printStackTrace();
          pendingRequests = null;
        }
      }

      // Thông báo thành công hoặc lỗi
      String message = request.getParameter("message");
      String error = request.getParameter("error");
    %>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty message}">
      <div class="success-message">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="error-message">${error}</div>
    </c:if>

    <div class="class-info-container">
      <!-- Hộp thông tin lớp học -->
      <div class="class-info-box">
        <h1>Chi tiết lớp học: <%= classEntity.getName() %></h1>
        <p><strong>Mô tả:</strong> <%= classEntity.getDescription() %></p>
        <p><strong>Mã lớp học:</strong> <%= classEntity.getClass_key() %></p>
        <p><strong>Giáo viên:</strong> <%= classEntity.getTeacher_name() %></p>
      </div>

      <!-- Hộp chứa các nút hành động (chỉ hiển thị cho giáo viên) -->
      <% if (isTeacher) { %>
      <div class="class-action-buttons">
        <!-- Nút chỉnh sửa lớp học -->
        <a href="${pageContext.request.contextPath}/EditClassServlet?classId=<%= classEntity.getId() %>" class="button edit-class-btn">
          <i class="fas fa-edit"></i> Chỉnh Sửa Lớp
        </a>
        <!-- Nút gán quiz -->
        <a href="${pageContext.request.contextPath}/AssignQuizServlet?classId=<%= classEntity.getId() %>" class="button assign-quiz-btn">
          <i class="fas fa-plus"></i> Gán Quiz
        </a>
        <!-- Nút tạo cuộc thi -->
        <a href="${pageContext.request.contextPath}/TakeCompetitionServlet?action=configure&classId=<%= classEntity.getId() %>" class="button create-competition-btn">
          <i class="fas fa-trophy"></i> Tạo Cuộc Thi
        </a>
        <a href="${pageContext.request.contextPath}/teacher/analytics" class="button btn-info">
          <i class="fas fa-chart-bar"></i> Phân tích Dữ liệu
        </a>
      </div>
      <% } %>

      <!-- Ô thông báo -->
      <div class="notification-area">
        <c:if test="${not empty message}">
          <div class="success-message">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
          <div class="error-message">${error}</div>
        </c:if>
      </div>
    </div>

    <!-- Danh sách yêu cầu tham gia (chỉ hiển thị cho giáo viên) -->
    <% if (isTeacher) { %>
    <section id="pending-requests">
      <h2>Yêu cầu tham gia lớp học</h2>
      <% if (pendingRequests != null && !pendingRequests.isEmpty()) { %>
      <table class="students-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Tên Sinh viên</th>
          <th>Email</th>
          <th>Thời gian gửi</th>
          <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <% for (JoinRequest jr : pendingRequests) { %>
        <tr>
          <td><%= jr.getUserId() %></td>
          <td><%= jr.getUsername() %></td>
          <td><%= jr.getEmail() %></td>
          <td><fmt:formatDate value="${jr.requestedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          <td>
            <!-- Nút chấp nhận -->
            <form action="${pageContext.request.contextPath}/HandleJoinRequestServlet" method="post" style="display:inline;">
              <input type="hidden" name="classId" value="<%= jr.getClassId() %>">
              <input type="hidden" name="userId" value="<%= jr.getUserId() %>">
              <input type="hidden" name="action" value="approve">
              <button type="submit" class="button approve-button">
                <i class="fas fa-check"></i> Chấp nhận
              </button>
            </form>
            <!-- Nút từ chối -->
            <form action="${pageContext.request.contextPath}/HandleJoinRequestServlet" method="post" style="display:inline;">
              <input type="hidden" name="classId" value="<%= jr.getClassId() %>">
              <input type="hidden" name="userId" value="<%= jr.getUserId() %>">
              <input type="hidden" name="action" value="reject">
              <button type="submit" class="button reject-button">
                <i class="fas fa-times"></i> Từ chối
              </button>
            </form>
          </td>
        </tr>
        <% } %>
        </tbody>
      </table>
      <% } else { %>
      <p>Không có yêu cầu tham gia nào.</p>
      <% } %>
    </section>
    <% } %>

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
            <form action="${pageContext.request.contextPath}/TakeQuizServlet" method="get">
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
            <th>Tên Sinh viên</th>
            <th>Email</th>
            <% if (isTeacher) { %>
            <th>Hành động</th>
            <% } %>
          </tr>
          </thead>
          <tbody>
          <% for (Users student : enrolledStudents) { %>
          <tr>
            <td><%= student.getUsername() %></td>
            <td><%= student.getEmail() %></td>
            <% if (isTeacher) { %>
            <td>
              <!-- Nút xóa sinh viên khỏi lớp học -->
              <form action="${pageContext.request.contextPath}/DeleteStudentServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sinh viên này khỏi lớp học?');">
                <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
                <input type="hidden" name="userId" value="<%= student.getId() %>">
                <button type="submit" class="button delete-button">
                  <i class="fas fa-trash"></i> Xóa
                </button>
              </form>
            </td>
            <% } %>
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
      <a href="${pageContext.request.contextPath}/jsp/teacher.jsp" class="button back-btn">
        <i class="fas fa-arrow-left"></i> Quay lại Lớp học
      </a>
    </div>
  </div>

  <!-- CSS tùy chỉnh -->
  <style>
    .class-action-buttons {
      margin-top: 20px;
    }
    .class-action-buttons .button {
      margin-right: 10px;
      margin-bottom: 10px;
    }
    .approve-button {
      background-color: #28a745;
      color: #fff;
      border: none;
      padding: 5px 10px;
      cursor: pointer;
    }
    .approve-button:hover {
      background-color: #218838;
    }
    .reject-button {
      background-color: #dc3545;
      color: #fff;
      border: none;
      padding: 5px 10px;
      cursor: pointer;
    }
    .reject-button:hover {
      background-color: #c82333;
    }
    .delete-button {
      background-color: #ff0000;
      color: #fff;
      border: none;
      padding: 5px 10px;
      cursor: pointer;
    }
    .delete-button:hover {
      background-color: #cc0000;
    }
  </style>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="${pageContext.request.contextPath}/js/common.js"></script>
</body>
</html>
