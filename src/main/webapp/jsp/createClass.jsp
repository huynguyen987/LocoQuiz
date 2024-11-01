<%@ page import="entity.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tạo Lớp Mới - QuizLoco</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-class.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css">
</head>
<style>
    /* Header and Footer Styles */
    .main-header, .main-footer {
        background-color: #2c529d;
        color: #fff;
        padding: 15px 20px;
        text-align: center;
    }

    .main-header .container, .main-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .main-header .logo {
        font-size: 1.5em;
        font-weight: bold;
        color: #fff;
    }

    .nav-menu a {
        color: #fff;
        margin: 0 15px;
        text-decoration: none;
        font-weight: 500;
    }

    .nav-menu a:hover {
        text-decoration: underline;
    }

    .main-footer p {
        font-size: 0.9em;
        margin: 0;
    }

    .create-class-container {
        max-width: 600px;
        margin: 40px auto;
        padding: 20px;
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    }

    .form-container label {
        font-weight: bold;
    }

    .submit-btn, .btn-back {
        margin-top: 15px;
        padding: 10px 15px;
        border-radius: 5px;
        font-size: 1em;
        color: #fff;
        background-color: #1d2a3f;
        display: inline-flex;
        align-items: center;
        text-decoration: none;
        gap: 5px;
    }

    .submit-btn:hover, .btn-back:hover {
        background-color: #4e4a4a;
    }

    /* Success and Error Messages */
    .success-message, .error-message, .info-message {
        padding: 10px;
        border-radius: 5px;
        margin-top: 15px;
    }

    .success-message {
        background-color: #d4edda;
        color: #155724;
    }

    .error-message {
        background-color: #f8d7da;
        color: #721c24;
    }

    .info-message {
        background-color: #d1ecf1;
        color: #0c5460;
    }

</style>
<body>
<!-- Header -->
<header class="main-header">
    <div class="container">
        <h1 class="logo">QuizLoco</h1>
        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/teacherDashboard?action=update">Dashboard</a>
            <a href="${pageContext.request.contextPath}/jsp/logout">Logout</a>
            <a href="${pageContext.request.contextPath}/jsp/user-profile">Profile</a>
        </nav>
    </div>
</header>

<!-- Main Content -->
<main class="create-class-container">
    <h2>Tạo Lớp Mới</h2>
    <form action="${pageContext.request.contextPath}/CreateClassServlet" method="post" class="form-container">
        <label for="name">Tên Lớp:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">Mô Tả:</label>
        <textarea id="description" name="description"></textarea>

        <button type="submit" class="submit-btn">
            <i class="fas fa-plus"></i> Tạo Lớp
        </button>
    </form>

    <div class="back-home">
        <a href="${pageContext.request.contextPath}/teacherDashboard?action=update" class="btn-back">
            <i class="fas fa-arrow-left"></i> Trở về Trang Chủ
        </a>
    </div>

    <!-- Display Messages -->
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>
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
</main>

<!-- Footer -->
<footer class="main-footer">
    <p>&copy; 2024 QuizLoco. All rights reserved.</p>
</footer>

</body>
</html>
