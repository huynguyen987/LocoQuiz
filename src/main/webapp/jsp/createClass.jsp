<%@ page import="entity.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Kiểm tra quyền hạn
    session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");
    if (currentUser == null || (currentUser.getRole_id() != 2 && currentUser.getRole_id() != 3)) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Tạo Lớp Mới</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/styles.css">
</head>
<body>
<div class="container">
    <h1>Tạo Lớp Mới</h1>
    <form action="<%=request.getContextPath()%>/CreateClassServlet" method="post">
        <label for="name">Tên Lớp:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">Mô Tả:</label>
        <textarea id="description" name="description"></textarea>

        <button type="submit">Tạo Lớp</button>
    </form>

    <div class="back-home">
        <a href="<%= request.getContextPath() %>/jsp/teacher.jsp" class="btn-back">Trở về Trang Chủ</a>
    </div>

    <% if (request.getAttribute("errorMessage") != null) { %>
    <p class="error-message"><%= request.getAttribute("errorMessage") %></p>
    <% } %>
</div>
</body>
</html>
