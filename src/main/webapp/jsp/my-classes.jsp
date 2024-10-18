<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.Users, entity.classs, dao.ClassDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>My Classes - QuizLoco</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
  <!-- Pass contextPath to JavaScript -->
  <script type="text/javascript">
    var contextPath = '<%= request.getContextPath() %>';
  </script>
</head>
<body>
<%@ include file="components/header.jsp" %>
<%@ include file="components/sidebar.jsp" %>

<main>
  <div class="dashboard-content">
    <%
      // Xác thực phiên và người dùng
      session = request.getSession(false);
      if (session != null) {
        currentUser = (Users) session.getAttribute("user");
      }
      if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRoleName())) {
        response.sendRedirect(request.getContextPath() + "/unauthorized.jsp");
        return;
      }

      // Lấy danh sách các lớp học đã đăng ký của sinh viên
      List<classs> enrolledClasses;
      String message = request.getParameter("message");
      String error = request.getParameter("error");
      try {
        enrolledClasses = new ClassDAO().getClassesByStudentId(currentUser.getId());
      } catch (SQLException | ClassNotFoundException e) {
        throw new ServletException(e);
      }
    %>

    <!-- Thông báo -->
    <% if (message != null) { %>
    <div class="success-message"><%= message %></div>
    <% } %>
    <% if (error != null) { %>
    <div class="error-message"><%= error %></div>
    <% } %>

    <!-- Phần nhập mã lớp học để tham gia -->
    <section id="join-class" class="join-class">
      <h1>Tham gia lớp học</h1>
      <form action="<%= request.getContextPath() %>/JoinClassServlet" method="post">
        <label for="classKey">Nhập mã lớp học:</label>
        <input type="text" id="classKey" name="classKey" required>
        <button type="submit">Tham gia lớp</button>
      </form>
    </section>

    <!-- Danh sách lớp học của tôi -->
    <section id="my-classes" class="my-classes">
      <h1>Các lớp học đã đăng ký</h1>
      <% if (enrolledClasses != null && !enrolledClasses.isEmpty()) { %>
      <div class="class-list">
        <% for (classs c : enrolledClasses) { %>
        <div class="class-item">
          <h2><%= c.getName() %></h2>
          <p><%= c.getDescription() %></p>
            <a href="<%= request.getContextPath() %>/ViewClassDetailsServlet?classId=<%= c.getId() %>" class="button">Xem chi tiết</a>
          <a href="<%= request.getContextPath() %>/ViewAssignedQuizzesServlet?classId=<%= c.getId() %>" class="button">Xem Quiz được giao</a>
        </div>
        <% } %>
      </div>
      <% } else { %>
      <p>Bạn chưa đăng ký lớp học nào.</p>
      <% } %>
    </section>
  </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
<script src="<%= request.getContextPath() %>/js/student.js"></script>
</body>
</html>
