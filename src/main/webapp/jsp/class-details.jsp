<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="entity.classs" %>
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
      classs classEntity = (classs) request.getAttribute("classEntity");
      if (classEntity == null) {
        response.sendRedirect(request.getContextPath() + "/my-classes.jsp?error=" + java.net.URLEncoder.encode("Không tìm thấy lớp học.", "UTF-8"));
        return;
      }
    %>
    <h1>Chi tiết lớp học: <%= classEntity.getName() %></h1>
    <p><strong>Mô tả:</strong> <%= classEntity.getDescription() %></p>

    <!-- Nút chuyển sang trang làm quiz -->
    <form action="<%= request.getContextPath() %>/TakeQuizServlet" method="get">
      <input type="hidden" name="classId" value="<%= classEntity.getId() %>">
      <button type="submit" class="button">Làm Quiz</button>
    </form>
  </div>
</main>

<%@ include file="components/footer.jsp" %>

<!-- JavaScript -->
<script src="<%= request.getContextPath() %>/js/common.js"></script>
</body>
</html>
