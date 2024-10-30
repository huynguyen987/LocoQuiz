<%@ page import="entity.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo Lớp Mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-class.css">
</head>
<body>
<div class="create-class-container">
    <h2>Tạo Lớp Mới</h2>
    <form action="${pageContext.request.contextPath}/CreateClassServlet" method="post">
        <label for="name">Tên Lớp:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">Mô Tả:</label>
        <textarea id="description" name="description"></textarea>

        <input type="submit" value="Tạo Lớp">
    </form>

    <div class="back-home">
        <a href="${pageContext.request.contextPath}/teacherDashboard?action=update" class="btn-back">Trở về Trang Chủ</a>
    </div>

    <!-- Hiển thị thông báo dựa trên request attribute 'errorMessage' -->
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <!-- Hiển thị thông báo thành công dựa trên tham số URL 'message' -->
    <c:if test="${not empty param.message}">
        <c:choose>
            <c:when test="${param.message == 'classCreated'}">
                <p class="success-message">Đã tạo lớp học thành công.</p>
            </c:when>
            <c:when test="${param.message == 'createError'}">
                <p class="error-message">Đã xảy ra lỗi khi tạo lớp học. Vui lòng thử lại.</p>
            </c:when>
            <c:otherwise>
                <p class="info-message">${param.message}</p>
            </c:otherwise>
        </c:choose>
    </c:if>
</div>
</body>
</html>
