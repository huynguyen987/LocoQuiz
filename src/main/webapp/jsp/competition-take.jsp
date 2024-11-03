<!-- File: /jsp/competition-take.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/jsp/components/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Lấy competitionId từ request parameter
    String competitionIdStr = request.getParameter("id");
    if (competitionIdStr == null || competitionIdStr.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/CompetitionController?action=list");
        return;
    }
%>
<%
    System.out.println("Current user: " + currentUser.getUsername());
    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }

    // Cho phép cả 'student' và 'teacher' tham gia cuộc thi
    if (!currentUser.hasRole("student") && !currentUser.hasRole("teacher")) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền tham gia cuộc thi.");
        return;
    }
%>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tham Gia Cuộc Thi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>

<!-- Truyền competitionId đến JavaScript -->
<input type="hidden" id="competition-id" value="<%= competitionIdStr %>" />

<div class="container">
    <h2>Tham Gia Cuộc Thi</h2>

    <div id="competition-container">
        <!-- Nội dung của cuộc thi sẽ được tạo bởi competition.js -->
    </div>
</div>

<script>
    // Đảm bảo contextPath được xác định để sử dụng trong competition.js
    var contextPath = '<%= request.getContextPath() %>';
</script>

<script src="${pageContext.request.contextPath}/js/competition.js"></script>
<%@ include file="/jsp/components/footer.jsp" %>
</body>
</html>
